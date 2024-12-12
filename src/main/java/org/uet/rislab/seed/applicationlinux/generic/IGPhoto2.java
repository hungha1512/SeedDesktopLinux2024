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
    int gp_camera_capture(Pointer camera, int captureType, Pointer file, Pointer context);
    int gp_camera_capture_preview(Pointer camera, Pointer file, Pointer context);

    // Camera Configuration
    int gp_camera_set_config_value_string(Pointer camera, Pointer context, String key, String value);

    // Capture Types
    int GP_CAPTURE_IMAGE = 0;   // Capture a still image
    int GP_CAPTURE_MOVIE = 1;   // Capture a movie
    int GP_CAPTURE_SOUND = 2;   // Capture sound
    int GP_CAPTURE_PREVIEW = 3; // Capture a preview frame

    int gp_camera_get_config(Pointer camera, PointerByReference widget, Pointer context);

    int gp_widget_get_child_by_name(Pointer widget, String name, PointerByReference child);

    int gp_widget_set_value(Pointer widget, String value);

    int gp_camera_set_config(Pointer camera, Pointer widget, Pointer context);

    int gp_camera_file_get(Pointer camera, String folder, String filename, int i, Pointer file, Pointer context);
}
