package com.example.wale.whalesfilepicker;

public interface PermissionsCallback {
    void success(String granted);

    void failure(String failed);
}
