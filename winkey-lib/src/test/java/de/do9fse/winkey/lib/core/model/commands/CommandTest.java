package de.do9fse.winkey.lib.core.model.commands;

import org.junit.jupiter.api.BeforeEach;

public class CommandTest {
    private String BASE_PACKAGE;

    @BeforeEach
    void setUp() {
        this.BASE_PACKAGE = this.getClass().getPackage().getName() + ".test";
    }


    // TODO Implement after refactoring
    /*
    @Test 
    void testSuite1() throws Exception {
        final List<Class<WinKeyCommand>> validCommandClasses = WinKeyCommand.validateCommands(BASE_PACKAGE + ".suite1");
        assertEquals(0, validCommandClasses.size());
    }

    @Test 
    void testSuite2() throws Exception {
        final List<Class<WinKeyCommand>> validCommandClasses = WinKeyCommand.validateCommands(BASE_PACKAGE + ".suite2");
        assertEquals(0, validCommandClasses.size());
    }

    @Test
    void testSuite3() throws Exception {
        final WinKeyRuntimeException e = assertThrows(WinKeyRuntimeException.class, () -> WinKeyCommand.validateCommands(BASE_PACKAGE + ".suite3"));

        final String expectedMessage = MessageFormat.format(
            "The following command classes are missing the @{0} annotation: {1}",
            CommandConfiguration.class.getSimpleName(),
            CommandWithoutAnnotation.class.getName()
        );
        assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    void testSuite4() throws Exception {
        final List<Class<WinKeyCommand>> validCommandClasses = WinKeyCommand.validateCommands(BASE_PACKAGE + ".suite4");
        assertEquals(0, validCommandClasses.size());
    }

    @Test
    void testSuite5() throws Exception {
        final List<Class<WinKeyCommand>> validCommandClasses = WinKeyCommand.validateCommands(BASE_PACKAGE + ".suite5");
        assertEquals(2, validCommandClasses.size());

        final CommandInfo commandWithoutResponseInfo = new AdminCommandWithoutResponse().getCommandInfo();
        assertEquals(EmptyResponse.class, commandWithoutResponseInfo.responseType());
        assertEquals(
            Set.of(WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2, WinKeyProtocolVersion.V3),
            commandWithoutResponseInfo.allowedProtocolVersions()
        );

        final CommandInfo commandWithResponseInfo = new AdminCommandWithResponse().getCommandInfo();
        assertEquals(WinKeyVersionResponse.class, commandWithResponseInfo.responseType());
        assertEquals(
            Set.of(WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2, WinKeyProtocolVersion.V3),
            commandWithResponseInfo.allowedProtocolVersions()
        );
    }
   
    @Test
    void productionCommandsDeclareProtocolsAndExpectedResponses() {
        final List<WinKeyCommand> commands = List.of(
            new CalibrateCommand(),
            new ResetCommand(),
            new HostOpenCommand(),
            new HostCloseCommand(),
            new EchoTestCommand('A'),
            new ReadPaddleA2DCommand(),
            new ReadSpeedA2DCommand(),
            new GetValuesCommand(),
            new ReservedCommand(),
            new GetCalibrationValueCommand(),
            new SetWK1ModeCommand(),
            new SetWK2ModeCommand(),
            new DumpEEPROMCommand(),
            new LoadEEPROMCommand(),
            new SendStandaloneMessageCommand(0),
            new SideToneControlCommand(false, SideToneFrequency.FREQUENCY_1000_HZ),
            new SideToneFrequencyCommand(SideToneFrequencyCommand.SideToneFrequency.FREQUENCY_940_HZ),
            new TextCommand("A")
        );

        commands.forEach(command -> {
            final CommandInfo commandInfo = command.getCommandInfo();
            assertEquals(false, commandInfo.allowedProtocolVersions().isEmpty());
            assertEquals(command.getClass(), commandInfo.commandClass());
        });

        assertEquals(DefaultsResponse.class, new GetValuesCommand().getCommandInfo().responseType());
        assertEquals(PaddleA2DResponse.class, new ReadPaddleA2DCommand().getCommandInfo().responseType());
        assertEquals(SpeedPotentiometerResponse.class, new ReadSpeedA2DCommand().getCommandInfo().responseType());
        assertEquals(CalibrationValueResponse.class, new GetCalibrationValueCommand().getCommandInfo().responseType());
        assertEquals(EEPROMDumpResponse.class, new DumpEEPROMCommand().getCommandInfo().responseType());
    }
    */

}
