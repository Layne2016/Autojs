package com.stardust.auojs.inrt.auto_web;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.stardust.app.GlobalAppContext;
import com.stardust.auojs.inrt.Pref;
import com.stardust.auojs.inrt.autojs.AutoJs;
import com.stardust.autojs.ScriptEngineService;
import com.stardust.autojs.execution.ExecutionConfig;
import com.stardust.autojs.project.ProjectLauncher;
import com.stardust.autojs.script.StringScriptSource;
//importClass(com.stardust.auojs.inrt.auto_web.AutoWebBridge);com.stardust.auojs.inrt.auto_web.AutoWebBridge.instance().getWebParams();
public class AutoWebBridge {


    private ScriptEngineService scriptEngineService = AutoJs.Companion.getInstance().getScriptEngineService();

    /**
     * 运行脚本
     * 示例:bridge.execAjWidthParams('toastLog("VUE run script","我是额外参数")')
     */
    @JavascriptInterface
    public void execAjWidthParams(String script, String params) {
        if (!TextUtils.isEmpty(params)) {
            Pref.INSTANCE.getPreferences().edit().putString(GlobalAppContext.get().getPackageName(), params).apply();
        }
        StringScriptSource scriptSource = new StringScriptSource(GlobalAppContext.get().getPackageName(), script);
        scriptEngineService.execute(scriptSource, new ExecutionConfig());
    }


    @JavascriptInterface
    public void execAj(String script) {
        StringScriptSource scriptSource = new StringScriptSource(GlobalAppContext.get().getPackageName(), script);
        scriptEngineService.execute(scriptSource, new ExecutionConfig());
    }

    /**
     * 运行项目(传入项目手机存储的位置)
     * 示例:bridge.runAJProjectByPath('/sdcard/project/示例项目')
     */
    @JavascriptInterface
    public void runAJProjectByPath(String dir) {
        new ProjectLauncher(dir).launch(scriptEngineService);
    }

    /**
     * 运行项目(项目名称)
     * 示例:bridge.runAJProjectByPath('/sdcard/project/示例项目')
     */
    @JavascriptInterface
    public void runAJProject(String name) {
        new ProjectLauncher("${Pref.getProjectPath()}/name").launch(scriptEngineService);
    }


    /**
     * android Logcat 输出日志
     * 示例:bridge.loge('from vue log')
     */
    @JavascriptInterface
    public void loge(String msg) {
        Log.e("bridge", msg);
    }

    @JavascriptInterface
    public void log(String msg) {
        Log.d("bridge", msg);
    }

    @JavascriptInterface
    public String getWebParams() {
        return Pref.INSTANCE.getPreferences().getString(GlobalAppContext.get().getPackageName(), null);
    }



    private AutoWebView autoWebView;

    public AutoWebView getAutoWebView() {
        return autoWebView;
    }

    public void setAutoWebView(AutoWebView autoWebView) {
        this.autoWebView = autoWebView;
    }

    private AutoWebBridge() {
    }

    private static final class Holder {
        private static AutoWebBridge instance = new AutoWebBridge();
    }

    public static AutoWebBridge instance() {
        return Holder.instance;
    }
}
