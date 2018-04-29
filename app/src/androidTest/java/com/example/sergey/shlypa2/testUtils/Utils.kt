package com.example.sergey.shlypa2.testUtils

import android.app.Activity
import android.app.Instrumentation
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage

/**
 * Created by alex on 4/29/18.
 */
class Utils {
    companion object {
        fun getCurrentActivity(instrumentation : Instrumentation) : Activity? {
            var currentActivity : Activity? = null
            instrumentation.runOnMainSync( {
                val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED)
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity = resumedActivities.iterator().next() as Activity
                }
            })

            return currentActivity
        }
    }
}