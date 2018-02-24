package com.xplor.android.utils;

import android.os.Bundle;

public interface Saveable {
    public void saveInstanceState(Bundle bundle);
    public void restoreInstanceState(Bundle bundle);
}
