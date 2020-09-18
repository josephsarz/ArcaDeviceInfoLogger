package com.femi.test;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.work.Configuration;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import com.femi.test.worker.DeviceInfoWorker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DeviceInfoWorkerTest {

    private Context myContext;

    @Before
    public void setup() {
        myContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
                myContext, config);
    }


    @Test
    public void testPeriodicWork() throws Exception {

        // Create request
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(DeviceInfoWorker.class, 15, MINUTES)
                        .build();

        WorkManager workManager = WorkManager.getInstance(myContext);
        TestDriver testDriver = WorkManagerTestInitHelper.getTestDriver(myContext);
        // Enqueue
        workManager.enqueue(request).getResult().get();
        // Tells the testing framework the period delay is met
        assert testDriver != null;
        testDriver.setPeriodDelayMet(request.getId());
        // Get WorkInfo and outputData
        WorkInfo workInfo = workManager.getWorkInfoById(request.getId()).get();
        // Assert
        assertThat(workInfo.getState(), is(WorkInfo.State.ENQUEUED));
    }
}