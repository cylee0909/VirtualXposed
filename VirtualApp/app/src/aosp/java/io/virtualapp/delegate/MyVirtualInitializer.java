package io.virtualapp.delegate;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.lody.virtual.client.core.VirtualCore;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.List;

/**
 * @author weishu
 * @date 2019/2/25.
 */
public class MyVirtualInitializer extends BaseVirtualInitializer {
    public MyVirtualInitializer(Application application, VirtualCore core) {
        super(application, core);
    }

    @Override
    public void onMainProcess() {
        AppCenter.start(application, "bf5e74bd-3795-49bd-95c8-327db494dd11",
                Analytics.class, Crashes.class);
        super.onMainProcess();
    }

    @Override
    public void onVirtualProcess() {
        List<PackageInfo> packageInfos =  VirtualCore.get().getUnHookPackageManager().getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo info = packageInfos.get(i);
                ApplicationInfo ai = info.applicationInfo;
                if (ai.metaData != null && ai.metaData.containsKey("xposedmodule")) {
                    VirtualCore.get().addVisibleOutsidePackage(info.packageName);
                }
            }
        }

        // For Crash statics
        AppCenter.start(application, "bf5e74bd-3795-49bd-95c8-327db494dd11",
                Analytics.class, Crashes.class);

        super.onVirtualProcess();

        // Override
        virtualCore.setCrashHandler(new MyCrashHandler());
    }
}
