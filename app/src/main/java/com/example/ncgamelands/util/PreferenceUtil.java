package com.example.ncgamelands.util;

import com.example.ncgamelands.prefrences.BasePreferenceUtil;

import info.metadude.android.typedpreferences.BooleanPreference;
import info.metadude.android.typedpreferences.StringPreference;


public class PreferenceUtil extends BasePreferenceUtil {

    public static StringPreference token() {
        return stringPreference("token");
    }

    public static StringPreference pushToken() {
        return stringPreference("push_token", "push_token");
    }

    public static StringPreference fcmToken() {
        return stringPreference("fcm_token", null);
    }

    public static StringPreference locale() {
        return stringPreference("locale", "en");
    }

    public static BooleanPreference isTrackingEnable() {
        return booleanPreference("is_tracking_enable", false);
    }


//    public static ModelPreference<User> user() {
//        return new ModelPreference<>(User.class);
//    }

    public static int getNotificationId() {
        int id = intPreference("notification_id", 100).get();
        id++;
        intPreference("notification_id").set(id);
        return id;
    }

/*    public static void setItemList(List<CustomInfo> models) {
        Gson gson = new Gson();
        try {
            String json = gson.toJson(models);
            stringPreference("item_list").set(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<CustomInfo> getItemList() {
        String json = stringPreference("item_list").get();
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            try {
                Type type = new TypeToken<ArrayList<CustomInfo>>() {
                }.getType();
                List<CustomInfo> models = gson.fromJson(json, type);
                return models;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public static void addUpdateVehicle(CustomInfo item) {
        if (item != null) {
            List<CustomInfo> models = getItemList();

            CustomInfo itemToUpdate = new CustomInfo();
            boolean isElementFound = false;

            for (CustomInfo item1 : models) {
                if (item1.getRegn_no().equalsIgnoreCase(item.getRegn_no())) {
                    itemToUpdate = item1;

                    isElementFound = true;
                    break;
                }
            }
            if (!isElementFound) {
                models.add(item);
            } else {
                models.remove(itemToUpdate);
                models.add(item);
            }

            setItemList(models);
        }
    }

    public static void removeItem(CustomInfo item) {
        if (item != null) {
            List<CustomInfo> models = getItemList();

            CustomInfo vehicleToRemove = new CustomInfo();
            boolean isElementFound = false;

            for (CustomInfo item1 : models) {
                if (item1.getRegn_no().equalsIgnoreCase(item.getRegn_no())) {
                    vehicleToRemove = item1;

                    isElementFound = true;
                    break;
                }
            }
            if (!isElementFound) {
            } else {
                models.remove(vehicleToRemove);
            }

            setItemList(models);
        }
    }*/

}