package pl.codo.rummager.service;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Disabled
class TransferServiceTest implements ISpeedTestListener {

    @Inject
    DownloadTransferService transferService;

    private final int maxDuration = 100000;

    @Test
    void measureTransfer() throws InterruptedException {
       // transferService.measureDownload("http://ipv4.ikoula.testdebit.info/100M.iso", maxDuration, this);
        synchronized (this) {
            wait();
        }


    }

    @Override
    public void onCompletion(SpeedTestReport report) {
        assertTrue(report.getTransferRateBit().compareTo(BigDecimal.ZERO) ==1);
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void onProgress(float percent, SpeedTestReport report) {

    }

    @Override
    public void onError(SpeedTestError speedTestError, String errorMessage) {
        fail("Error while downloading file");
        synchronized (this) {
            notify();
        }
    }
}