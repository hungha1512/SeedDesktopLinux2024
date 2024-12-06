package org.uet.rislab.seed.applicationlinux.generic;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IGPhoto2Lib extends Library {
    IGPhoto2Lib INSTANCE = Native.load("gphoto2", IGPhoto2Lib.class);

    //Init context
    Pointer gp_context_new();

    //Create camera
    int gp_camera_new(PointerByReference camera);

    //Init camera
    int gp_camera_init(Pointer camera, Pointer context);

    //Exit camera
    int gp_camera_exit(Pointer camera, Pointer context);

    //Free camera memory
    int gp_camera_free(Pointer camera);

    //Constant for capture_preview
    int GP_CAPTURE_PREVIEW = 1;

    //Create file
    int gp_file_new(PointerByReference file);

    //Live view camera
    int gp_camera_capture_preview(Pointer camera, Pointer file, Pointer context);

    //Get data and size
    int gp_file_get_data_and_size(Pointer file, PointerByReference data, NativeLongByReference size);

    //Free file
    int gp_file_free(Pointer file);
}
