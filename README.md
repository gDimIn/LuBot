# LuBot
TelegaBot
// Global
dictionary slotByBtn; // btn -> Slot@

class Item {
    int typeId;
    string iconPath;
    int stack;
    int maxStack;

    Item(int t, const string &in icon, int s, int maxs) {
        typeId = t; iconPath = icon; stack = s; maxStack = maxs;
    }
}

// -------------------------
// Slot: icon (BACKDROP) + btn + stack text
// -------------------------
class Slot {
    int index;
    Item@ item;

    framehandle backdrop;
    framehandle btn;
    framehandle stackText;

    ScrollInventory@ owner;

    Slot(ScrollInventory@ o, int idx, framehandle parent, float slotSize, float slotGap, int cols) {
        @owner = o;
        index = idx;

        // create backdrop (icon)
        backdrop = CreateFrameByType("BACKDROP", "SlotBack"+index, parent, "", 0);
        FrameSetSize(backdrop, slotSize, slotSize);

        // position will be set by owner.updatePositions()
        // create stack text as child
        stackText = CreateFrameByType("TEXT", "SlotText"+index, backdrop, "", 0);
        FrameSetPoint(stackText, FRAMEPOINT_BOTTOMRIGHT, backdrop, FRAMEPOINT_BOTTOMRIGHT, -0.002, 0.002);
        FrameSetText(stackText, "");
        FrameSetTextAlignment(stackText, TEXTALIGN_RIGHT, TEXTALIGN_BOTTOM);

        // create button on top (ScriptDialogButton works reliably in 1.26a)
        btn = CreateFrame("ScriptDialogButton", parent, 0, 0);
        FrameSetSize(btn, slotSize, slotSize);
        FrameSetAllPoints(btn, backdrop); // align to backdrop
        FrameSetTexture(btn, "", 0, true); // remove default look
        FrameSetAlpha(btn, 0);             // transparent

        // map btn -> this slot
        slotByBtn[btn] = @this;

        // register click
        trigger t = CreateTrigger();
        TriggerRegisterFrameEvent(t, btn, FRAMEEVENT_CONTROL_CLICK);
        TriggerAddAction(t, function OnSlotClick);
    }

    bool isEmpty() { return item is null; }

    void setItem(Item@ it) {
        @item = it;
        if (it !is null) {
            FrameSetTexture(backdrop, it.iconPath, 0, true);
            if (it.stack > 1) FrameSetText(stackText, "" + it.stack);
            else FrameSetText(stackText, "");
        } else {
            // default empty texture
            FrameSetTexture(backdrop, "UI\\Widgets\\EscMenu\\Human\\blank-background.blp", 0, true);
            FrameSetText(stackText, "");
        }
    }

    // hide/show UI (we hide all 3 frames)
    void setVisible(bool v) {
        FrameSetVisible(backdrop, v);
        FrameSetVisible(btn, v);
        FrameSetVisible(stackText, v);
    }
}

// -------------------------
// ScrollInventory
// -------------------------
class ScrollInventory {
    framehandle parent;       // parent UI (e.g. GetGameUI())
    framehandle containerBG;  // visible area (backdrop)
    framehandle topMask;      // mask overlay (top)
    framehandle bottomMask;   // mask overlay (bottom)
    framehandle slider;       // vertical slider
    framehandle upBtn;        // optional up button
    framehandle downBtn;      // optional down button

    array<Slot@> slots;

    int totalSlots;
    int cols;
    int totalRows;
    int visibleRows;
    int offsetRow; // current top row index (0-based)

    float slotSize;
    float slotGap;

    // constructor: parent frame, totalSlots, columns, how many visible rows
    ScrollInventory(framehandle parent_, int totalSlots_, int cols_, int visibleRows_, float slotSize_ = 0.06, float slotGap_ = 0.07) {
        @parent = parent_;
        totalSlots = totalSlots_;
        cols = cols_;
        visibleRows = visibleRows_;
        slotSize = slotSize_;
        slotGap = slotGap_;

        totalRows = I2I((totalSlots + cols - 1) / cols); // ceil
        offsetRow = 0;

        // create container background
        containerBG = CreateFrameByType("BACKDROP", "InvBG", parent, "", 0);
        FrameSetSize(containerBG, cols * slotGap + 0.01, visibleRows * slotGap + 0.01);
        FrameSetPoint(containerBG, FRAMEPOINT_CENTER, parent, FRAMEPOINT_CENTER, 0.0, 0.0);
        // set background (UjAPI uses SetFrameBackdropTexture for backdrop)
        SetFrameBackdropTexture(containerBG, "UI\\Widgets\\EscMenu\\Human\\blank-background.blp", 0, true);

        // create top mask and bottom mask (they will sit above slot visuals and hide overflow)
        topMask = CreateFrameByType("BACKDROP", "TopMask", parent, "", 0);
        FrameSetSize(topMask, cols * slotGap + 0.01, 0.02);
        FrameSetPoint(topMask, FRAMEPOINT_BOTTOMLEFT, containerBG, FRAMEPOINT_TOPLEFT, 0.0, 0.0);
        SetFrameBackdropTexture(topMask, "UI\\Widgets\\EscMenu\\Human\\blank-background.blp", 0, true);

        bottomMask = CreateFrameByType("BACKDROP", "BotMask", parent, "", 0);
        FrameSetSize(bottomMask, cols * slotGap + 0.01, 0.02);
        FrameSetPoint(bottomMask, FRAMEPOINT_TOPLEFT, containerBG, FRAMEPOINT_BOTTOMLEFT, 0.0, 0.0);
        SetFrameBackdropTexture(bottomMask, "UI\\Widgets\\EscMenu\\Human\\blank-background.blp", 0, true);

        // slider (right side)
        slider = CreateFrameByType("SLIDER", "InvSlider", parent, "", 0);
        FrameSetSize(slider, 0.015, visibleRows * slotGap);
        FrameSetPoint(slider, FRAMEPOINT_TOPLEFT, containerBG, FRAMEPOINT_TOPRIGHT, 0.006, 0.0);
        // configure slider range (rows)
        SetFrameMinMaxValue(slider, 0, I2I(max(0, totalRows - visibleRows)));
        SetFrameStepSize(slider, 1);
        SetFrameValue(slider, 0);

        // up/down buttons
        upBtn = CreateFrame("ScriptDialogButton", parent, 0, 0);
        FrameSetSize(upBtn, 0.02, 0.02);
        FrameSetPoint(upBtn, FRAMEPOINT_BOTTOMLEFT, containerBG, FRAMEPOINT_TOPRIGHT, 0.006, 0.002);
        FrameSetText(upBtn, "▲");

        downBtn = CreateFrame("ScriptDialogButton", parent, 0, 0);
        FrameSetSize(downBtn, 0.02, 0.02);
        FrameSetPoint(downBtn, FRAMEPOINT_TOPLEFT, containerBG, FRAMEPOINT_BOTTOMRIGHT, 0.006, -0.002);
        FrameSetText(downBtn, "▼");

        // create slot objects (position updated in updatePositions)
        for (int i = 0; i < totalSlots; i++) {
            Slot s = Slot(this, i, parent, slotSize, slotGap, cols);
            slots.insertLast(s);
        }

        // register slider event
        trigger tS = CreateTrigger();
        TriggerRegisterFrameEvent(tS, slider, FRAMEEVENT_SLIDER_VALUE_CHANGED);
        TriggerAddAction(tS, function onSliderChanged);

        // register up/down buttons
        trigger tUp = CreateTrigger();
        TriggerRegisterFrameEvent(tUp, upBtn, FRAMEEVENT_CONTROL_CLICK);
        TriggerAddAction(tUp, function onUpClick);

        trigger tDown = CreateTrigger();
        TriggerRegisterFrameEvent(tDown, downBtn, FRAMEEVENT_CONTROL_CLICK);
        TriggerAddAction(tDown, function onDownClick);

        // initial layout
        updatePositions();
    }

    // update positions of all slot frames according to offsetRow
    void updatePositions() {
        for (uint i = 0; i < slots.length(); i++) {
            int row = I2I(i / cols);
            int col = i % cols;

            // y offset: row index relative to top of visible area = row - offsetRow
            float y = -(row - offsetRow) * slotGap;

            // place backdrop and button
            FrameSetPoint(slots[i].backdrop, FRAMEPOINT_TOPLEFT, containerBG, FRAMEPOINT_TOPLEFT, col * slotGap + 0.005, y - 0.005);
            FrameSetPoint(slots[i].btn, FRAMEPOINT_TOPLEFT, containerBG, FRAMEPOINT_TOPLEFT, col * slotGap + 0.005, y - 0.005);
            FrameSetPoint(slots[i].stackText, FRAMEPOINT_BOTTOMRIGHT, slots[i].backdrop, FRAMEPOINT_BOTTOMRIGHT, -0.002, 0.002);

            // visibility if within visible rows
            if (row >= offsetRow && row < offsetRow + visibleRows) {
                slots[i].setVisible(true);
            } else {
                slots[i].setVisible(false);
            }
        }

        // update slider value (avoid recursion: only set if differs)
        int curVal = R2I(GetFrameValue(slider));
        if (curVal != offsetRow) SetFrameValue(slider, offsetRow);
    }

    // slider callback will call this
    void setOffset(int newOffset) {
        int maxOff = max(0, totalRows - visibleRows);
        if (newOffset < 0) newOffset = 0;
        if (newOffset > maxOff) newOffset = maxOff;
        offsetRow = newOffset;
        updatePositions();
    }

    // add item into inventory logic (summing stacks)
    void addItem(Item@ it) {
        if (it is null) return;

        // 1) try to add to existing stacks
        for (uint i = 0; i < slots.length(); i++) {
            if (!slots[i].isEmpty() && slots[i].item.typeId == it.typeId) {
                int free = slots[i].item.maxStack - slots[i].item.stack;
                if (free <= 0) continue;
                if (it.stack <= free) {
                    slots[i].item.stack += it.stack;
                    // update UI only if visible
                    if (isIndexVisible(i)) slots[i].setItem(slots[i].item);
                    return;
                } else {
                    slots[i].item.stack = slots[i].item.maxStack;
                    it.stack -= free;
                    if (isIndexVisible(i)) slots[i].setItem(slots[i].item);
                }
            }
        }

        // 2) place in empty slots (possibly multiple if stack overflows)
        for (uint i = 0; i < slots.length(); i++) {
            if (it.stack <= 0) break;
            if (slots[i].isEmpty()) {
                int toPut = min(it.stack, it.maxStack);
                Item@ newIt = Item(it.typeId, it.iconPath, toPut, it.maxStack);
                slots[i].setItem(newIt); // sets item and UI if visible
                it.stack -= toPut;
                // if slot not visible, UI will not show until updatePositions() when offset changes
            }
        }

        // 3) if still leftover, drop to world (implement your dropNearUnit)
        if (it.stack > 0) {
            // dropNearUnit implementation depends on your engine code
            // Example: CreateItem(it.typeId, GetUnitX(u)+40, GetUnitY(u))
            // For now we just print
            print("Inventory full — leftover: " + it.stack);
        }
    }

    bool isIndexVisible(int i) {
        int row = I2I(i / cols);
        return (row >= offsetRow && row < offsetRow + visibleRows);
    }
}

// -------------------------
// Callbacks
// -------------------------
void onSliderChanged() {
    int val = R2I(GetFrameValue(GetTriggerFrame()));
    // But GetTriggerFrame returns slider frame only if we registered slider trigger as trigger frame.
    // Simpler: use global reference to slider: assume single scroll inventory active
    // (if multiple — store mapping)
    // Here we use global name 'activeScroll' if you keep it, or pass closure. For simplicity:
    extern ScrollInventory@ activeScroll;
    if (activeScroll !is null) activeScroll.setOffset(val);
}

void onUpClick() {
    extern ScrollInventory@ activeScroll;
    if (activeScroll !is null) activeScroll.setOffset(activeScroll.offsetRow - 1);
}

void onDownClick() {
    extern ScrollInventory@ activeScroll;
    if (activeScroll !is null) activeScroll.setOffset(activeScroll.offsetRow + 1);
}

// Slot click handling
void OnSlotClick() {
    framehandle clicked = GetTriggerFrame();
    Slot@ s;
    if (slotByBtn.get(clicked, @s) && s !is null) {
        if (!s.isEmpty()) {
            // example: drop item
            print("Clicked slot idx=" + s.index + " item type=" + s.item.typeId + " stack=" + s.item.stack);
            // s.owner.dropSlot(s.index); // implement drop if needed
        }
    }
}
