package com.example.ncgamelands.prefrences;

import java.util.List;

/**
 * Created by Ivan on 11/4/2015.
 */
public abstract class BaseListPreference<T> {

    protected Class<T> clazz;
    public String key;

    public BaseListPreference(Class<T> clazz) {
        this.clazz = clazz;
    }

    public BaseListPreference(Class<T> clazz, String key) {
        this.clazz = clazz;
        this.key = key;
    }

    public List<T> get() {
        String serialized = BasePreferenceUtil.stringPreference(getKey()).get();
        try {
            return parse(serialized);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(List<T> list) {
        if (list == null) {
            BasePreferenceUtil.stringPreference(getKey()).set("");
            return;
        }
        try {
            BasePreferenceUtil.stringPreference(getKey()).set(serialize(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKey() {
        return key == null ? clazz.getCanonicalName() : key;
    }

    //Use any json parsing library, or serialize/parse manually
    public abstract String serialize(List<T> list) throws Exception;

    public abstract List<T> parse(String jsonString) throws Exception;
}
