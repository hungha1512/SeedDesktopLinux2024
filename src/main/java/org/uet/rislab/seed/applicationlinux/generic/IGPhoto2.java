package org.uet.rislab.seed.applicationlinux.generic;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IGPhoto2 extends Library {
    IGPhoto2 INSTANCE = Native.load("gphoto2", IGPhoto2.class);

    // Context Methods
    Pointer gp_context_new();
    void gp_context_unref(Pointer context);

    // Camera Methods
    int gp_camera_new(PointerByReference camera);
    int gp_camera_init(Pointer camera, Pointer context);
    int gp_camera_exit(Pointer camera, Pointer context);
    int gp_camera_free(Pointer camera);

    // File Management
    int gp_file_new(PointerByReference file);
    int gp_file_free(Pointer file);
    int gp_file_get_data_and_size(Pointer file, PointerByReference data, NativeLongByReference size);

    // Capture Methods
    int gp_camera_capture_preview(Pointer camera, Pointer file, Pointer context);
}
