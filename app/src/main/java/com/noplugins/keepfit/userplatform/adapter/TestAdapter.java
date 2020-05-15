package com.noplugins.keepfit.userplatform.adapter;

public class TestAdapter {

    OnItemSelect onItemSelect;

    public OnItemSelect getOnItemSelect() {
        return onItemSelect;
    }

    public void setOnItemSelect(OnItemSelect onItemSelect) {
        this.onItemSelect = onItemSelect;
    }

    private interface OnItemSelect{
        void selected(int pos);
    }
}
