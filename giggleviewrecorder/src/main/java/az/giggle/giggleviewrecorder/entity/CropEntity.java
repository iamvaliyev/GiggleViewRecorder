package az.giggle.giggleviewrecorder.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Emil Valiyev on 6/3/16.
 */
public class CropEntity implements Parcelable {

    float widht;
    float height;
    float x;
    float y;

    public CropEntity(float widht, float height, float x, float y) {
        this.widht = widht;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public float getWidht() {
        return widht;
    }

    public void setWidht(float widht) {
        this.widht = widht;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    protected CropEntity(Parcel in) {
        widht = in.readFloat();
        height = in.readFloat();
        x = in.readFloat();
        y = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(widht);
        dest.writeFloat(height);
        dest.writeFloat(x);
        dest.writeFloat(y);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CropEntity> CREATOR = new Parcelable.Creator<CropEntity>() {
        @Override
        public CropEntity createFromParcel(Parcel in) {
            return new CropEntity(in);
        }

        @Override
        public CropEntity[] newArray(int size) {
            return new CropEntity[size];
        }
    };
}