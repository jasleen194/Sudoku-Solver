import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GUI extends Solver {

    private JFrame frame;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton startButton;
    private JButton stopButton;
    private Timer timer;
    private JLabel timerLabel;
    private JTextField[] fields;

    public GUI() {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(600, 650)); // Adjusting to square size with timer

        JPanel panel = new JPanel();

        b1 = new JButton("Solve");
        b1.addActionListener(e -> {
            // Calling a function that takes the inputted value from the UI and in turn
            // calls more
            // methods to solve and update the UI with solved values
            GUIToSudoku(fields);
        });

        b2 = new JButton("Clear grid");
        b2.addActionListener(e -> {
            // clearing all JTextFields by setting them ""
            for (int i = 0; i < 81; i++) {
                fields[i].setText("");
                fields[i].setBackground(Color.WHITE);
                fields[i].setForeground(Color.PINK);
            }
        });

        b3 = new JButton("Exit");
        b3.addActionListener(e -> {
            // disposing the frame
            frame.dispose();
            System.out.println("Exited");
        });

        startButton = new JButton("Start Timer");
        startButton.addActionListener(e -> startTimer());

        stopButton = new JButton("Stop Timer");
        stopButton.addActionListener(e -> stopTimer());

        timerLabel = new JLabel("Timer: 00:00", JLabel.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b3);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        fields = getClearedFields();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(9, 9)); // Adjusted to 9x9 grid
        for (int x = 0; x < 81; x++) {
            panel.add(fields[x]);
        }

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(timerLabel, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sudoku Solver");
        frame.pack();
        frame.setVisible(true);

        timer = new Timer(1000, new ActionListener() {
            private int secondsPassed = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                int minutes = secondsPassed / 60;
                int seconds = secondsPassed % 60;
                timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds));
            }
        });
    }

    void startTimer() {
        timer.start();
    }

    void stopTimer() {
        timer.stop();
    }

    JTextField[] getClearedFields() {
        JTextField[] newFields = new JTextField[81];
        for (int x = 0; x < 81; x++) {
            newFields[x] = new JTextField();
            JTextField f = newFields[x];
            f.setHorizontalAlignment(JTextField.CENTER);
            f.setBackground(Color.PINK);
            f.setEditable(true);

            int row = x / 9;
            int col = x % 9;
            Border border;
            if (row % 3 == 0 && col % 3 == 0) {
                border = BorderFactory.createMatteBorder(3, 3, 1, 1, Color.WHITE);
            } else if (row % 3 == 0) {
                border = BorderFactory.createMatteBorder(3, 1, 1, 1, Color.WHITE);
            } else if (col % 3 == 0) {
                border = BorderFactory.createMatteBorder(1, 3, 1, 1, Color.WHITE);
            } else {
                border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE);
            }

            // Adjust borders for last row and column
            if (row == 8) {
                if (col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 3, 3, 1, Color.WHITE);
                } else if (col == 8) {
                    border = BorderFactory.createMatteBorder(1, 1, 3, 3, Color.WHITE);
                } else {
                    border = BorderFactory.createMatteBorder(1, 1, 3, 1, Color.WHITE);
                }
            } else if (col == 8) {
                if (row % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 1, 1, 3, Color.WHITE);
                } else {
                    border = BorderFactory.createMatteBorder(1, 1, 1, 3, Color.WHITE);
                }
            }

            f.setBorder(border);

            f.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    char pressedKey = ke.getKeyChar();
                    if (pressedKey == 8) {
                        // backspace
                        f.setText("");
                        f.setBackground(Color.PINK);
                    } else if ((pressedKey >= '1' && pressedKey <= '9')) {
                        // after setting text we set editable false as automatically key listener would
                        // also add the value
                        // as soon as key is released which will lead to repeated value
                        f.setText("" + pressedKey);
                        f.setEditable(false);
                        f.setBackground(Color.LIGHT_GRAY);
                    } else {
                        f.setEditable(false);
                    }
                }
            });
            // finally we set editable back to true
            f.setEditable(true);
        }

        return newFields;
    }

    void populateFieldInteractive(JTextField f, int numberToUpdate, int initial) {
        f.setEditable(true);
        if (initial == 0) {
            f.setForeground(Color.LIGHT_GRAY);
        } else {
            f.setForeground(Color.WHITE);
        }
        f.setText("" + numberToUpdate);
        f.setEditable(false);
    }

    void SudokuToGUI(int[][] solution, int[][] initialSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                populateFieldInteractive(fields[(i * 9) + j], solution[i][j], initialSudoku[i][j]);
            }
        }
    }

    void copySudoku(int[][] originalSudoku, int[][] copiedSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copiedSudoku[i][j] = originalSudoku[i][j];
            }
        }
    }

    void GUIToSudoku(JTextField[] fields) {
        SudokuClass inputPuzzle = new SudokuClass();
        boolean isValid = true;

        // Clear all background colors
        for (JTextField field : fields) {
            field.setBackground(Color.GRAY);
        }

        // Populate SudokuClass from GUI input
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String val = fields[9 * i + j].getText();
                if (val.equals("")) {
                    inputPuzzle.sudoku[i][j] = 0;
                } else {
                    int num = Integer.parseInt(val);
                    inputPuzzle.sudoku[i][j] = num;

                    // Check if the number already exists in the row, column, or 3x3 grid
                    if (!isValid(num, i, j, inputPuzzle.sudoku)) {
                        fields[9 * i + j].setBackground(Color.RED);
                        isValid = false;
                    }
                }
            }
        }

        // If Sudoku is valid, proceed to solve and update GUI
        if (isValid && isValid(inputPuzzle)) {
            int[][] initialSudoku = new int[9][9];
            copySudoku(inputPuzzle.sudoku, initialSudoku);

            int[][] solution = new int[9][9];
            solveAndGetFirstSolution(inputPuzzle, 0, 0, solution);

            SudokuToGUI(solution, initialSudoku);
        } else {
            // Display error message
            JOptionPane.showMessageDialog(frame, "Invalid value entered! Please check the Sudoku rules.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    boolean isValid(int num, int row, int col, int[][] sudoku) {
        // Check row
        for (int i = 0; i < 9; i++) {
            if (i != col && sudoku[row][i] == num) {
                return false;
            }
        }

        // Check column
        for (int i = 0; i < 9; i++) {
            if (i != row && sudoku[i][col] == num) {
                return false;
            }
        }

        // Check 3x3 grid
        int gridRowStart = (row / 3) * 3;
        int gridColStart = (col / 3) * 3;
        for (int i = gridRowStart; i < gridRowStart + 3; i++) {
            for (int j = gridColStart; j < gridColStart + 3; j++) {
                if (i != row && j != col && sudoku[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
