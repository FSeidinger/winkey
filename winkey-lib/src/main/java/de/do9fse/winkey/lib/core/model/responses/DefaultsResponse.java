package de.do9fse.winkey.lib.core.model.responses;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.do9fse.winkey.lib.core.model.commands.host.immediate.SideToneControlCommand.SideToneFrequency;
import de.do9fse.winkey.lib.core.model.configuration.DitDahRatio;
import de.do9fse.winkey.lib.core.model.configuration.FarnsworthSpeed;
import de.do9fse.winkey.lib.core.model.configuration.FirstExtensionDelay;
import de.do9fse.winkey.lib.core.model.configuration.KeyCompensation;
import de.do9fse.winkey.lib.core.model.configuration.LeadInDelay;
import de.do9fse.winkey.lib.core.model.configuration.ModeRegister;
import de.do9fse.winkey.lib.core.model.configuration.PaddleSetpoint;
import de.do9fse.winkey.lib.core.model.configuration.PinConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.TailDelay;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeedRange;
import de.do9fse.winkey.lib.core.model.configuration.Weighting;

@ResponseConfiguration(
    expectedResponseBytes = 15
)
public record DefaultsResponse(
    ModeRegister mode,
    WPMSpeed wpmSpeed,
    SideToneFrequency sideToneFrequency,
    Weighting weighting,
    LeadInDelay leadInDelay,
    TailDelay tailDelay,
    WPMSpeed wpmMinimum,
    WPMSpeedRange wpmRange,
    FirstExtensionDelay firstExtensionDelay,
    KeyCompensation keyCompensation,
    FarnsworthSpeed farnsworthSpeed,
    PaddleSetpoint paddleSetpoint,
    DitDahRatio ditDahRatio,
    PinConfiguration pinConfiguration,
    int reservedValue
) implements Response {
    public static DefaultsResponse fromData(final byte[] responseBytes) {
        final int[] responses = Response.toUnsignedInts(responseBytes);

        return new DefaultsResponse(
            ModeRegister.fromProtocol(responses[0]),
            WPMSpeed.fromProtocol(responses[1]),
            SideToneFrequency.parseResponseByte(responses[2]),
            Weighting.fromProtocol(responses[3]),
            LeadInDelay.fromProtocol(responses[4]),
            TailDelay.fromProtocol(responses[5]),
            WPMSpeed.fromProtocol(responses[6]),
            WPMSpeedRange.fromProtocol(responses[7]),
            FirstExtensionDelay.fromProtocol(responses[8]),
            KeyCompensation.fromProtocol(responses[9]),
            FarnsworthSpeed.fromProtocol(responses[10]),
            PaddleSetpoint.fromProtocol(responses[11]),
            DitDahRatio.fromProtocol(responses[12]),
            PinConfiguration.fromProtocol(responses[13]),
            responses[14]
        );
    }
}
