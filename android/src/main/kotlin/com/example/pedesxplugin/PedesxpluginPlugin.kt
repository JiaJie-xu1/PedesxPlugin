package com.example.pedesxplugin

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.adsmobile.pedesxsdk.ui.activity.LookVideoActivity
import com.adsmobile.pedesxsdk.utils.ActivityUtils
import com.adsmobile.pedesxsdk.utils.PedesxUtil
import com.qq.e.ads.cfg.MultiProcessFlag
import com.qq.e.comm.managers.GDTADManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** PedesxpluginPlugin */
public class PedesxpluginPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var applicationContext: Context
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)

    }

    fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
        this.applicationContext = applicationContext
        channel = MethodChannel(messenger, "pedesxplugin")
        channel.setMethodCallHandler(this)
    }

    fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger, activity: Activity) {
        this.applicationContext = applicationContext
        channel = MethodChannel(messenger, "pedesxplugin")
        channel.setMethodCallHandler(this)
        this.activity = activity
    }


    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val instance = PedesxpluginPlugin()
            instance.onAttachedToEngine(registrar.context(), registrar.messenger(), registrar.activity())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        Log.e("PedesxPlugin", "call method:" + call.method)
        MultiProcessFlag.setMultiProcess(true);
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "registerPedesx") {
            val appId: String? = call.argument("appId")
            val shelf_id: String? = call.argument("shelf_id")
            val csj_appId: String? = call.argument("csj_appId")
            val csj_video_id: String? = call.argument("csj_video_id")
            val ylh_appId: String? = call.argument("ylh_appId")
            val ylh_video_id: String? = call.argument("ylh_video_id")

            Log.e("PedesxPlugin", "ylh_appId:$ylh_appId")
            Log.e("PedesxPlugin", "ylh_video_id:$ylh_video_id")
            PedesxUtil.init(applicationContext, appId, shelf_id, csj_appId, csj_video_id, ylh_appId, ylh_video_id)
        } else if (call.method == "registerPedesxUser") {
            val uid: String? = call.argument("uid")
            val oaid: String? = call.argument("oaid")
            PedesxUtil.initUser(applicationContext, uid, oaid)
        } else if (call.method == "startPedesxVideoActivity") {
            ActivityUtils.startActivity(activity, LookVideoActivity::class.java)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {

    }
}
