package az.giggle.giggleviewrecorder.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Emil Valiyev on 6/29/16.
 */
public class RecorderSettings implements Parcelable {

    CropEntity cropEntity;

    boolean withDuration = false;
    boolean isNotify = true;
    int duration = 8000;

    public RecorderSettings(CropEntity cropEntity) {
        this.cropEntity = cropEntity;
    }

    public RecorderSettings(CropEntity cropEntity, boolean withDuration, boolean isNotify, int duration) {
        this.cropEntity = cropEntity;
        this.withDuration = withDuration;
        this.isNotify = isNotify;
        this.duration = duration;
    }

    public RecorderSettings(View view) {
        setViewSizes(view);
    }

    public RecorderSettings(View view, boolean withDuration, boolean isNotify, int duration) {
        this.withDuration = withDuration;
        this.isNotify = isNotify;
        this.duration = duration;
        setViewSizes(view);
    }

    public CropEntity getCropEntity() {
        return cropEntity;
    }

    public void setCropEntity(CropEntity cropEntity) {
        this.cropEntity = cropEntity;
    }

    public boolean isWithDuration() {
        return withDuration;
    }

    public void setWithDuration(boolean withDuration) {
        this.withDuration = withDuration;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected RecorderSettings(Parcel in) {
        cropEntity = (CropEntity) in.readValue(CropEntity.class.getClassLoader());
        withDuration = in.readByte() != 0x00;
        isNotify = in.readByte() != 0x00;
        duration = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cropEntity);
        dest.writeByte((byte) (withDuration ? 0x01 : 0x00));
        dest.writeByte((byte) (isNotify ? 0x01 : 0x00));
        dest.writeInt(duration);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecorderSettings> CREATOR = new Parcelable.Creator<RecorderSettings>() {
        @Override
        public RecorderSettings createFromParcel(Parcel in) {
            return new RecorderSettings(in);
        }

        @Override
        public RecorderSettings[] newArray(int size) {
            return new RecorderSettings[size];
        }
    };

    public void setViewSizes(View view) {
        int[] locations = new int[2];
        view.getLocationInWindow(locations);
        this.cropEntity = new CropEntity(view.getMeasuredWidth(), view.getMeasuredHeight(), locations[0], locations[1]);
    }
}