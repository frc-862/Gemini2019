package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.TimedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lightning.commands.VelocityMotionProfile;
import frc.robot.Robot;
import frc.robot.commands.AutonLineFollow;
import frc.robot.commands.LineFollow;
import frc.robot.commands.cargo.EjectElevatorCargo;
import frc.robot.commands.driveTrain.WaitForDriverOK;
import frc.robot.commands.elevator.SetElevatorLow;
import frc.robot.commands.hatch.CloseHatchCollector;
import frc.robot.commands.hatch.ExtendHatchCollector;
import frc.robot.commands.hatch.OpenHatchCollector;
import frc.robot.commands.hatch.RetractHatchCollector;

public class Auto extends CommandGroup {

    private String piece = "HATCH";// two vals HATCH & CARGO
    private String pathFile;
    private Command elevatorPos;

    //Choosers: Command elevator, String gamepiece
    public Auto(Command elevatorPos, String inPiece, String start, String genDestin, String specificDestin, Boolean doNothing) {

        //if (!doNothing) {
        requires(Robot.drivetrain);
        requires(Robot.elevator);
        requires(Robot.hatchPanelCollector);
        requires(Robot.cargoCollector);

        this.pathFile = selectPath(start, genDestin, specificDestin);
        System.out.println("path "+pathFile);
        SmartDashboard.putString("PATH: ", pathFile);
        initPiece(inPiece);
        this.elevatorPos = elevatorPos;
        initPiecePos();

        //waitForDriverOK();

        getToTarget();
        linefollow();
        //deployPiece();
        //}

    }



    public Auto(Command elevatorPos, String path, String inPiece, Boolean doNothing) {

        //if (!doNothing) {
        requires(Robot.drivetrain);
        requires(Robot.elevator);
        requires(Robot.hatchPanelCollector);
        requires(Robot.cargoCollector);

        this.pathFile = path;
        System.out.println("path "+pathFile);
        SmartDashboard.putString("PATH: ", pathFile);
        initPiece(inPiece);
        this.elevatorPos = elevatorPos;
        initPiecePos();

        //waitForDriverOK();

        getToTarget();
        addSequential(new LineFollow());

        //linefollow();
        //deployPiece();
        //}

    }



    private void linefollow() {
        addSequential(new AutonLineFollow());
    }

    private void waitForDriverOK() {
        addSequential(new WaitForDriverOK());
    }

    private String selectPath(String start, String genDestin, String specificDestin) {
        String selectedPath = "";//default left rocket near
        switch(genDestin) {
        case "ROCK_RIGHT":
            selectedPath += ("RocketR_" + selectStart(start, specificDestin));
            break;
        case "SHIP_FRONT":
            selectedPath += ("CargoC_" + selectStart(start, specificDestin));
            break;
        case "SHIP_LEFT":
            selectedPath += ("CargoL_" + selectStart(start, specificDestin));
            break;
        case "SHIP_RIGHT":
            selectedPath += ("CargoR_" + selectStart(start, specificDestin));
            break;
        default://ROCK_LEFT
            selectedPath += ("RocketL_" + selectStart(start, specificDestin));
            break;
        }
        return selectedPath;
    }

    private String selectStart(String start, String specificDestin) {
        String nextSeg = "";
        switch(start) {
        case "CENTER":
            nextSeg += ("StartM_" + selectDestin(specificDestin));
            break;
        case "RIGHT":
            nextSeg += ("StartR_" + selectDestin(specificDestin));
            break;
        default://LEFT
            nextSeg += ("StartL_" + selectDestin(specificDestin));
            break;
        }
        return nextSeg;
    }

    private String selectDestin(String specificDestin) {
        String nextSeg = "";
        switch(specificDestin) {
        case "FAR":
            nextSeg += ("EndF");
            break;
        case "RIGHT":
            nextSeg += ("EndR");
            break;
        case "LEFT":
            nextSeg += ("EndL");
            break;
        case "MID":
            nextSeg += ("EndM");
            break;
        default://NEAR
            nextSeg += ("EndN");
            break;
        }
        return nextSeg;
    }

    private void deployPiece() {
        if(this.piece.equals("CARGO")) {
            addSequential(new EjectElevatorCargo());
        } else { //Hatch
            addSequential(new ExtendHatchCollector());
            waitForAction(0.25);
            addSequential(new OpenHatchCollector());
            waitForAction(0.25);
            addSequential(new RetractHatchCollector());
        }
    }

    private void getToTarget() {
        addParallel(new VelocityMotionProfile(this.pathFile));
        addParallel(this.elevatorPos);
    }

    private void initPiecePos() {
        if(this.piece.equals("CARGO")) {

        } else { // This gets the hatch out of the robot. We don't need to do anything for cargo.
            addSequential(new LineFollow());
            addSequential(new CloseHatchCollector());
            addSequential(new SetElevatorLow());
        }
    }

    private void initPiece(String piece) {
        if(piece.equals("CARGO")) this.piece = "CARGO";
        else this.piece = "HATCH";
    }

    private void waitForAction(double secs) {
        addSequential(new TimedCommand(secs));
    }

}
