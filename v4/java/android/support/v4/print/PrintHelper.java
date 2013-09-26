/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.net.Uri;

import java.io.FileNotFoundException;

/**
 * Helper for printing bitmaps.
 */
public final class PrintHelper {
    /**
     * image will be scaled but leave white space
     */
    public static final int SCALE_MODE_FIT = 1;
    /**
     * image will fill the paper and be cropped (default)
     */
    public static final int SCALE_MODE_FILL = 2;
    PrintHelperVersionImpl mImpl;

    /**
     * Gets whether the system supports printing.
     *
     * @return True if printing is supported.
     */
    public static boolean systemSupportsPrint() {
        if ("KeyLimePie".equals(Build.VERSION.CODENAME) // TODO recode to be KitKat and above
                || Build.VERSION.SDK_INT > 18) {

            return true;
        }
        return false;
    }

    /**
     * Interface implemented by classes that support printing
     */
    static interface PrintHelperVersionImpl {

        public void setScaleMode(int scaleMode);

        public int getScaleMode();

        public void printBitmap(String jobName, Bitmap bitmap);

        public void printBitmap(String jobName, Uri imageFile)
                throws FileNotFoundException;
    }

    /**
     * Implementation used when we do not support printing
     */
    private static final class PrintHelperStubImpl implements PrintHelperVersionImpl {
        int mScaleMode;

        @Override
        public void setScaleMode(int scaleMode) {
            mScaleMode = scaleMode;
        }

        @Override
        public int getScaleMode() {
            return mScaleMode;
        }

        @Override
        public void printBitmap(String jobName, Bitmap bitmap) {
        }

        @Override
        public void printBitmap(String jobName, Uri imageFile) {
        }
    }

    /**
     * Implementation used on KitKat (and above)
     */
    private static final class PrintHelperKitkatImpl implements PrintHelperVersionImpl {
        private final PrintHelperKitkat printHelper;

        PrintHelperKitkatImpl(Context context) {
            printHelper = new PrintHelperKitkat(context);
        }

        @Override
        public void setScaleMode(int scaleMode) {
            printHelper.setScaleMode(scaleMode);
        }

        @Override
        public int getScaleMode() {
            return printHelper.getScaleMode();
        }

        @Override
        public void printBitmap(String jobName, Bitmap bitmap) {
            printHelper.printBitmap(jobName, bitmap);
        }

        @Override
        public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
            printHelper.printBitmap(jobName, imageFile);
        }
    }

    /**
     * Returns the PrintHelper that can be used to print images.
     *
     * @param context A context for accessing system resources.
     * @return the <code>PrintHelper</code> to support printing images.
     */
    public PrintHelper(Context context) {
        if ("KeyLimePie".equals(Build.VERSION.CODENAME)  // TODO recode to be KitKat and above
                || Build.VERSION.SDK_INT > 18) {
            mImpl = new PrintHelperKitkatImpl(context);
        } else {
            mImpl = new PrintHelperStubImpl();
        }
    }

    /**
     * Selects whether the image will fill the paper and be cropped
     * {@link #SCALE_MODE_FIT}
     * or whether the image will be scaled but leave white space
     * {@link #SCALE_MODE_FILL}.
     *
     * @param scaleMode {@link #SCALE_MODE_FIT} or
     *                  {@link #SCALE_MODE_FILL}
     */
    public void setScaleMode(int scaleMode) {
        mImpl.setScaleMode(scaleMode);
    }

    /**
     * Returns the scale mode with which the image will fill the paper.
     *
     * @return The scale Mode: {@link #SCALE_MODE_FIT} or
     * {@link #SCALE_MODE_FILL}
     */
    public int getScaleMode() {
        return mImpl.getScaleMode();
    }

    /**
     * Prints a bitmap.
     *
     * @param jobName The print job name.
     * @param bitmap  The bitmap to print.
     */
    public void printBitmap(String jobName, Bitmap bitmap) {
        mImpl.printBitmap(jobName, bitmap);
    }

    /**
     * Prints an image located at the Uri. Image types supported are those of
     * {@link android.graphics.BitmapFactory#decodeStream(java.io.InputStream)
     * android.graphics.BitmapFactory.decodeStream(java.io.InputStream)}
     *
     * @param jobName   The print job name.
     * @param imageFile The <code>Uri</code> pointing to an image to print.
     * @throws FileNotFoundException if <code>Uri</code> is not pointing to a valid image.
     */
    public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
        mImpl.printBitmap(jobName, imageFile);
    }
}