/*
 *
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.iot.agent.services;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.iot.agent.AndroidAgentException;
import org.wso2.iot.agent.R;
import org.wso2.iot.agent.api.ApplicationManager;
import org.wso2.iot.agent.api.WiFiConfig;
import org.wso2.iot.agent.beans.AppRestriction;
import org.wso2.iot.agent.beans.DeviceAppInfo;
import org.wso2.iot.agent.beans.Operation;
import org.wso2.iot.agent.utils.CommonUtils;
import org.wso2.iot.agent.utils.Constants;
import org.wso2.iot.agent.utils.Preference;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to revoke the existing policy on the device.
 */
public class PolicyRevokeHandler {

    private static final String TAG = PolicyOperationsMapper.class.getSimpleName();
    private Context context;
    private DevicePolicyManager devicePolicyManager;
    private Resources resources;
    private ComponentName deviceAdmin;
    private ApplicationManager applicationManager;

    public PolicyRevokeHandler(Context context) {
        this.context = context;
        this.resources = context.getResources();
        this.devicePolicyManager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.deviceAdmin = new ComponentName(context, AgentDeviceAdminReceiver.class);
        this.applicationManager = new ApplicationManager(context.getApplicationContext());
    }

    /**
     * Revokes EMM policy on the device.
     *
     * @param operation - Operation object.
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void revokeExistingPolicy(org.wso2.iot.agent.beans.Operation operation)
            throws AndroidAgentException {

        if (applicationManager.isPackageInstalled(Constants.SYSTEM_SERVICE_PACKAGE)) {
            switch (operation.getCode()) {
                case Constants.Operation.CAMERA:
                    revokeCameraPolicy(operation);
                    break;
                case Constants.Operation.INSTALL_APPLICATION:
                    revokeInstallAppPolicy(operation);
                    break;
                case Constants.Operation.ENCRYPT_STORAGE:
                    revokeEncryptPolicy(operation);
                    break;
                case Constants.Operation.PASSCODE_POLICY:
                    revokePasswordPolicy();
                    break;
                case Constants.Operation.WIFI:
                    revokeWifiPolicy(operation);
                    break;
                case Constants.Operation.COSU_PROFILE_POLICY:
                    revokeCOSUProfilePolicy(operation);
                    break;
                case Constants.Operation.DISALLOW_ADJUST_VOLUME:
                case Constants.Operation.DISALLOW_CONFIG_BLUETOOTH:
                case Constants.Operation.DISALLOW_CONFIG_CELL_BROADCASTS:
                case Constants.Operation.DISALLOW_CONFIG_CREDENTIALS:
                case Constants.Operation.DISALLOW_CONFIG_MOBILE_NETWORKS:
                case Constants.Operation.DISALLOW_CONFIG_TETHERING:
                case Constants.Operation.DISALLOW_CONFIG_VPN:
                case Constants.Operation.DISALLOW_CONFIG_WIFI:
                case Constants.Operation.DISALLOW_APPS_CONTROL:
                case Constants.Operation.DISALLOW_CREATE_WINDOWS:
                case Constants.Operation.DISALLOW_CROSS_PROFILE_COPY_PASTE:
                case Constants.Operation.DISALLOW_DEBUGGING_FEATURES:
                case Constants.Operation.DISALLOW_FACTORY_RESET:
                case Constants.Operation.DISALLOW_ADD_USER:
                case Constants.Operation.DISALLOW_INSTALL_APPS:
                case Constants.Operation.DISALLOW_INSTALL_UNKNOWN_SOURCES:
                case Constants.Operation.DISALLOW_MODIFY_ACCOUNTS:
                case Constants.Operation.DISALLOW_MOUNT_PHYSICAL_MEDIA:
                case Constants.Operation.DISALLOW_NETWORK_RESET:
                case Constants.Operation.DISALLOW_OUTGOING_BEAM:
                case Constants.Operation.DISALLOW_OUTGOING_CALLS:
                case Constants.Operation.DISALLOW_REMOVE_USER:
                case Constants.Operation.DISALLOW_SAFE_BOOT:
                case Constants.Operation.DISALLOW_SHARE_LOCATION:
                case Constants.Operation.DISALLOW_SMS:
                case Constants.Operation.DISALLOW_UNINSTALL_APPS:
                case Constants.Operation.DISALLOW_UNMUTE_MICROPHONE:
                case Constants.Operation.DISALLOW_USB_FILE_TRANSFER:
                case Constants.Operation.ALLOW_PARENT_PROFILE_APP_LINKING:
                case Constants.Operation.ENSURE_VERIFY_APPS:
                case Constants.Operation.AUTO_TIME:
                case Constants.Operation.ENABLE_ADMIN:
                case Constants.Operation.SET_SCREEN_CAPTURE_DISABLED:
                case Constants.Operation.SET_STATUS_BAR_DISABLED:
                     CommonUtils.callSystemApp(context, operation.getCode(),
                                Boolean.toString(false), null);
                     break;
                case Constants.Operation.APP_RESTRICTION:
                    revokeAppRestrictionPolicy(operation);
                    break;
                case Constants.Operation.RUNTIME_PERMISSION_POLICY:
                    revokeRunTimePermissionPolicyOperation(operation);
                default:
                    throw new AndroidAgentException("Invalid operation code received");
            }
        } else {
            switch (operation.getCode()) {
                case Constants.Operation.CAMERA:
                    revokeCameraPolicy(operation);
                    break;
                case Constants.Operation.INSTALL_APPLICATION:
                    revokeInstallAppPolicy(operation);
                    break;
                case Constants.Operation.ENCRYPT_STORAGE:
                    revokeEncryptPolicy(operation);
                    break;
                case Constants.Operation.PASSCODE_POLICY:
                    revokePasswordPolicy();
                    break;
                case Constants.Operation.WIFI:
                    revokeWifiPolicy(operation);
                    break;
                case Constants.Operation.APP_RESTRICTION:
                    revokeAppRestrictionPolicy(operation);
                    break;
                case Constants.Operation.DISALLOW_ADJUST_VOLUME:
                case Constants.Operation.DISALLOW_SMS:
                case Constants.Operation.DISALLOW_CONFIG_CELL_BROADCASTS:
                case Constants.Operation.DISALLOW_CONFIG_BLUETOOTH:
                case Constants.Operation.DISALLOW_CONFIG_MOBILE_NETWORKS:
                case Constants.Operation.DISALLOW_CONFIG_TETHERING:
                case Constants.Operation.DISALLOW_CONFIG_WIFI:
                case Constants.Operation.DISALLOW_SAFE_BOOT:
                case Constants.Operation.DISALLOW_OUTGOING_CALLS:
                case Constants.Operation.DISALLOW_MOUNT_PHYSICAL_MEDIA:
                case Constants.Operation.DISALLOW_CREATE_WINDOWS:
                case Constants.Operation.DISALLOW_FACTORY_RESET:
                case Constants.Operation.DISALLOW_REMOVE_USER:
                case Constants.Operation.DISALLOW_ADD_USER:
                case Constants.Operation.DISALLOW_NETWORK_RESET:
                case Constants.Operation.DISALLOW_USB_FILE_TRANSFER:
                case Constants.Operation.DISALLOW_UNMUTE_MICROPHONE:
                    revokeDeviceOwnerRestrictionPolicy(operation);
                    break;
                case Constants.Operation.DISALLOW_CONFIG_CREDENTIALS:
                case Constants.Operation.DISALLOW_CONFIG_VPN:
                case Constants.Operation.DISALLOW_APPS_CONTROL:
                case Constants.Operation.DISALLOW_CROSS_PROFILE_COPY_PASTE:
                case Constants.Operation.DISALLOW_DEBUGGING_FEATURES:
                case Constants.Operation.DISALLOW_INSTALL_APPS:
                case Constants.Operation.DISALLOW_INSTALL_UNKNOWN_SOURCES:
                case Constants.Operation.DISALLOW_MODIFY_ACCOUNTS:
                case Constants.Operation.DISALLOW_OUTGOING_BEAM:
                case Constants.Operation.DISALLOW_SHARE_LOCATION:
                case Constants.Operation.DISALLOW_UNINSTALL_APPS:
                case Constants.Operation.ALLOW_PARENT_PROFILE_APP_LINKING:
                case Constants.Operation.ENSURE_VERIFY_APPS:
                    revokeOwnersRestrictionPolicy(operation);
                    break;
                case Constants.Operation.AUTO_TIME:
                    revokeAutoTimeRestrictionPolicy();
                    break;
                case Constants.Operation.SET_SCREEN_CAPTURE_DISABLED:
                    revokeScreenCaptureDisabledPolicy();
                    break;
                case Constants.Operation.SET_STATUS_BAR_DISABLED:
                    revokeStatusBarDisabledPolicy();
                    break;
                case Constants.Operation.COSU_PROFILE_POLICY:
                    revokeCOSUProfilePolicy(operation);
                    break;
                default:
                    throw new AndroidAgentException("Invalid operation code received");
            }
        }
    }

    /**
     * Revokes camera policy on the device.
     *
     * @param operation - Operation object.
     */
    private void revokeCameraPolicy(org.wso2.iot.agent.beans.Operation operation) {
        if (!operation.isEnabled()) {
            devicePolicyManager.setCameraDisabled(deviceAdmin, false);
        }
    }

    /**
     * Revokes install app policy on the device (Particular app in the policy should be removed).
     *
     * @param operation - Operation object.
     */
    private void revokeInstallAppPolicy(org.wso2.iot.agent.beans.Operation operation) throws AndroidAgentException {

        String appIdentifier = null;

        try {
            JSONObject appData = new JSONObject(operation.getPayLoad().toString());

            if (!appData.isNull(resources.getString(R.string.app_identifier))) {
                appIdentifier = appData.getString(resources.getString(R.string.app_identifier));
            }

            if (isAppInstalled(appIdentifier)) {
                applicationManager.uninstallApplication(appIdentifier, null);
            }

        } catch (JSONException e) {
            throw new AndroidAgentException("Invalid JSON format.", e);
        }
    }

    /**
     * Revoke app restriction policy (black list or white list).
     *
     * @param operation - Operation object
     * @throws AndroidAgentException
     */
    private void revokeAppRestrictionPolicy(org.wso2.iot.agent.beans.Operation operation)
            throws AndroidAgentException {

        AppRestriction appRestriction =
                CommonUtils.getAppRestrictionTypeAndList(operation, null, null);

        if (Constants.AppRestriction.BLACK_LIST.equals(appRestriction.getRestrictionType())) {
            for (String packageName : appRestriction.getRestrictedList()) {
                CommonUtils.callSystemApp(context, operation.getCode(), "true", packageName);
            }
        } else if (Constants.AppRestriction.WHITE_LIST.equals(appRestriction.getRestrictionType())) {
            List<String> installedAppPackages = CommonUtils.getAppsOfUser(context);
            List<String> toBeUnHideApps = new ArrayList<>(installedAppPackages);
            toBeUnHideApps.removeAll(appRestriction.getRestrictedList());
            for (String packageName : toBeUnHideApps) {
                CommonUtils.callSystemApp(context, operation.getCode(), "true", packageName);
            }
        }

    }

    /**
     * Checks if the app is already installed on the device.
     *
     * @param appIdentifier - App package name.
     * @return appInstalled - App installed status.
     */
    private boolean isAppInstalled(String appIdentifier) {
        boolean appInstalled = false;
        ArrayList<DeviceAppInfo> apps = new ArrayList<>(applicationManager.getInstalledApps().values());
        for (DeviceAppInfo appInfo : apps) {
            if (appIdentifier.trim().equals(appInfo.getPackagename())) {
                appInstalled = true;
            }
        }

        return  appInstalled;
    }

    /**
     * Revokes device encrypt policy on the device (Device external storage encryption).
     *
     * @param operation - Operation object.
     */
    private void revokeEncryptPolicy(org.wso2.iot.agent.beans.Operation operation) {

        boolean encryptStatus = (devicePolicyManager.getStorageEncryptionStatus() != devicePolicyManager.
                ENCRYPTION_STATUS_UNSUPPORTED && (devicePolicyManager.getStorageEncryptionStatus() == devicePolicyManager.
                ENCRYPTION_STATUS_ACTIVE || devicePolicyManager.getStorageEncryptionStatus() == devicePolicyManager.
                ENCRYPTION_STATUS_ACTIVATING));

        if (operation.isEnabled() && encryptStatus) {
            devicePolicyManager.setStorageEncryption(deviceAdmin, false);
        }
    }

    /**
     * Revokes screen lock password policy on the device.
     */
    private void revokePasswordPolicy() {
        devicePolicyManager.setPasswordQuality(deviceAdmin,
                                               DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        devicePolicyManager.setMaximumFailedPasswordsForWipe(deviceAdmin, 0);
        devicePolicyManager.setPasswordExpirationTimeout(deviceAdmin, 0);
        devicePolicyManager.setPasswordMinimumLength(deviceAdmin, 0);
    }

    /**
     * Revokes Wifi policy on the device (Particular wifi configuration in the policy should be disabled).
     *
     * @param operation - Operation object.
     */
    private void revokeWifiPolicy(org.wso2.iot.agent.beans.Operation operation) throws AndroidAgentException {
        String ssid = null;

        try {
            JSONObject wifiData = new JSONObject(operation.getPayLoad().toString());
            if (!wifiData.isNull(resources.getString(R.string.intent_extra_ssid))) {
                ssid = (String) wifiData.get(resources.getString(R.string.intent_extra_ssid));
            }

            WiFiConfig config = new WiFiConfig(context.getApplicationContext());
            if (config.findWifiConfigurationBySsid(ssid)) {
                config.removeWifiConfigurationBySsid(ssid);
            }
        } catch (JSONException e) {
            throw new AndroidAgentException("Invalid JSON format.", e);
        }
    }

    /**
     * Revokes Restriction policy on the device (Particular wifi configuration in the policy should be disabled).
     *
     * @param operation - Operation object.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revokeOwnersRestrictionPolicy(org.wso2.iot.agent.beans.Operation operation) throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE) ||
                devicePolicyManager.isProfileOwnerApp(Constants.AGENT_PACKAGE)) {
            devicePolicyManager.clearUserRestriction(deviceAdmin,getPermissionConstantValue(operation.getCode()));
        }
    }

    private String getPermissionConstantValue(String key) {
        return context.getString(resources.getIdentifier(
                key.toString(),"string",context.getPackageName()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void revokeDeviceOwnerRestrictionPolicy(org.wso2.iot.agent.beans.Operation operation) throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE)) {
            devicePolicyManager.clearUserRestriction(deviceAdmin,getPermissionConstantValue(operation.getCode()));
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revokeScreenCaptureDisabledPolicy() throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE) ||
                devicePolicyManager.isProfileOwnerApp(Constants.AGENT_PACKAGE)) {
            if (devicePolicyManager.getScreenCaptureDisabled(deviceAdmin)) {
                devicePolicyManager.setScreenCaptureDisabled(deviceAdmin, false);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void revokeStatusBarDisabledPolicy() throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE)) {
            devicePolicyManager.setStatusBarDisabled(deviceAdmin, false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void revokeAutoTimeRestrictionPolicy() throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE)) {
            devicePolicyManager.setAutoTimeRequired(deviceAdmin, false);
        }
    }

    private void revokeCOSUProfilePolicy(Operation operation) throws AndroidAgentException {
        try {
            JSONObject COSUProfileData = new JSONObject(operation.getPayLoad().toString());
            int lockDownTime = Preference.getInt(context, Constants.PreferenceCOSUProfile.FREEZE_TIME);
            int releaseTime = Preference.getInt(context, Constants.PreferenceCOSUProfile.RELEASE_TIME);
            int payloadLockTime = Integer.parseInt(COSUProfileData.
                    get(Constants.COSUProfilePolicy.deviceFreezeTime).toString());
            int payloadReleaseTime = Integer.parseInt(COSUProfileData.
                    get(Constants.COSUProfilePolicy.deviceReleaseTime).toString());
            if((payloadLockTime == lockDownTime) && (payloadReleaseTime== releaseTime) &&
                        Preference.getBoolean(context, Constants.PreferenceCOSUProfile.ENABLE_LOCKDOWN)){
                Preference.putBoolean(context, Constants.PreferenceCOSUProfile.ENABLE_LOCKDOWN,false);
            }

        } catch (JSONException e) {
            throw new AndroidAgentException("Invalid JSON format.", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void revokeRunTimePermissionPolicyOperation(Operation operation) throws AndroidAgentException {
        if (devicePolicyManager.isDeviceOwnerApp(Constants.AGENT_PACKAGE) ||
                devicePolicyManager.isProfileOwnerApp(Constants.AGENT_PACKAGE)) {
                devicePolicyManager.setPermissionPolicy(deviceAdmin, DevicePolicyManager.PERMISSION_POLICY_PROMPT);
                Preference.putString(context,Constants.RuntimePermissionPolicy.PERMITTED_APP_DATA, "");
        }
    }

}
