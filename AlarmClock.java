import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.*;

public class AlarmClock {
    private JFrame frame;
    private JLabel timeLabel;
    private JLabel alarmLabel;
    private Timer timer;
    private Clip bellSound;

    public AlarmClock() {
        // Load bell sound (built-in)
        loadBellSound();

        // Set up GUI
        frame = new JFrame("Hourly Alarm Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(new FlowLayout());

        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 32));

        alarmLabel = new JLabel("Waiting for next hour...", SwingConstants.CENTER);
        alarmLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        frame.add(timeLabel);
        frame.add(alarmLabel);
        frame.setVisible(true);

        // Check time every second
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);
    }

    private void loadBellSound() {
        try {
            // Use built-in system beep as fallback
            InputStream soundStream = getClass().getResourceAsStream("/bell.wav");
            if (soundStream != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundStream);
                bellSound = AudioSystem.getClip();
                bellSound.open(audioIn);
            }
        } catch (Exception e) {
            System.out.println("Using system beep (custom sound not found)");
        }
    }

    private void updateTime() {
        // Update clock display
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(new Date());
        timeLabel.setText(currentTime);

        // Check if it's exactly HH:00:00
        SimpleDateFormat checkFormat = new SimpleDateFormat("mm:ss");
        String checkTime = checkFormat.format(new Date());

        if (checkTime.equals("00:00")) {
            triggerHourlyAlarm();
        }
    }

    private void triggerHourlyAlarm() {
        String hour = new SimpleDateFormat("HH").format(new Date());
        alarmLabel.setText("Ringing for " + hour + ":00!");

        // Play sound
        playBellSound();

        // Show notification
        JOptionPane.showMessageDialog(frame,
                "It's " + hour + ":00!\nHourly bell ringing.",
                "Alarm",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void playBellSound() {
        try {
            if (bellSound != null) {
                bellSound.setFramePosition(0); // Rewind to start
                bellSound.start();
            } else {
                Toolkit.getDefaultToolkit().beep(); // Fallback
            }
        } catch (Exception e) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlarmClock());
    }
}