
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.UUID;

public class OnlineReservationSystem {

    private static final ArrayList<Reservation> reservations =
            new ArrayList<>();

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Use default look and feel.
            }

            new LoginFrame().setVisible(true);
        });
    }

    private static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent, message, "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static JTextField field(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return field;
    }

    private static JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(18, 22, 18, 22));
        return panel;
    }

    private static GridBagConstraints constraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    private static void addField(
            JPanel panel,
            GridBagConstraints c,
            int row,
            String label,
            JComponent input) {

        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(input, c);
    }

    // RESERVATION MODEL
    private static class Reservation {
        String pnr;
        String passengerName;
        int trainNumber;
        String trainName;
        String classType;
        LocalDate journeyDate;
        String source;
        String destination;

        Reservation(
                String pnr,
                String passengerName,
                int trainNumber,
                String trainName,
                String classType,
                LocalDate journeyDate,
                String source,
                String destination) {

            this.pnr = pnr;
            this.passengerName = passengerName;
            this.trainNumber = trainNumber;
            this.trainName = trainName;
            this.classType = classType;
            this.journeyDate = journeyDate;
            this.source = source;
            this.destination = destination;
        }

        @Override
        public String toString() {
            return "PNR: " + pnr +
                    "\nPassenger: " + passengerName +
                    "\nTrain: " + trainNumber + " - " + trainName +
                    "\nClass: " + classType +
                    "\nJourney date: " + journeyDate +
                    "\nSource: " + source +
                    "\nDestination: " + destination;
        }
    }

    // LOGIN WINDOW
    private static class LoginFrame extends JFrame {

        LoginFrame() {
            setTitle("Online Reservation System - Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(440, 270);
            setLocationRelativeTo(null);
            setResizable(false);

            JPanel panel = formPanel();
            GridBagConstraints c = constraints();

            JLabel heading = new JLabel("ONLINE RESERVATION SYSTEM");
            heading.setFont(new Font("SansSerif", Font.BOLD, 18));

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            panel.add(heading, c);

            c.gridwidth = 1;
            c.anchor = GridBagConstraints.WEST;

            JTextField username = field(18);
            JPasswordField password = new JPasswordField(18);

            addField(panel, c, 1, "Username:", username);
            addField(panel, c, 2, "Password:", password);

            JButton login = new JButton("Login");
            c.gridx = 1;
            c.gridy = 3;
            c.anchor = GridBagConstraints.EAST;
            panel.add(login, c);

            login.addActionListener(e -> {
                String user = username.getText().trim();
                String pass = new String(password.getPassword());

                if (USERNAME.equals(user) && PASSWORD.equals(pass)) {
                    dispose();
                    new MainFrame(user).setVisible(true);
                } else {
                    showError(this, "Invalid username or password.");
                }
            });

            password.addActionListener(e -> login.doClick());
            add(panel);
        }
    }

    // MAIN WINDOW
    private static class MainFrame extends JFrame {

        MainFrame(String username) {
            setTitle("Online Reservation System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(780, 590);
            setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Book Reservation", new ReservationPanel());
            tabs.addTab("Cancel Reservation", new CancellationPanel());

            JLabel footer = new JLabel(
                    "Logged in as: " + username,
                    SwingConstants.CENTER
            );
            footer.setBorder(new EmptyBorder(7, 5, 7, 5));

            add(tabs, BorderLayout.CENTER);
            add(footer, BorderLayout.SOUTH);
        }
    }

    // TICKET BOOKING
    private static class ReservationPanel extends JPanel {

        private final JTextField passenger = field(22);
        private final JTextField trainNumber = field(12);
        private final JTextField trainName = field(22);
        private final JTextField journeyDate = field(12);
        private final JTextField source = field(18);
        private final JTextField destination = field(18);

        private final JComboBox<String> classType = new JComboBox<>(
                new String[]{
                        "Sleeper",
                        "AC 3 Tier",
                        "AC 2 Tier",
                        "AC First Class",
                        "General"
                }
        );

        ReservationPanel() {
            setLayout(new BorderLayout());

            JPanel form = formPanel();
            GridBagConstraints c = constraints();

            JLabel heading = new JLabel("Book a Train Reservation");
            heading.setFont(new Font("SansSerif", Font.BOLD, 18));

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            form.add(heading, c);
            c.gridwidth = 1;

            trainName.setEditable(false);

            addField(form, c, 1, "Passenger name:", passenger);
            addField(form, c, 2, "Train number:", trainNumber);
            addField(form, c, 3, "Train name:", trainName);
            addField(form, c, 4, "Class:", classType);
            addField(form, c, 5,
                    "Journey date (YYYY-MM-DD):", journeyDate);
            addField(form, c, 6, "Source station:", source);
            addField(form, c, 7, "Destination station:", destination);

            JButton book = new JButton("Book Ticket");
            c.gridx = 1;
            c.gridy = 8;
            form.add(book, c);

            trainNumber.addActionListener(e -> updateTrainName());
            trainNumber.addFocusListener(
                    new java.awt.event.FocusAdapter() {
                        @Override
                        public void focusLost(
                                java.awt.event.FocusEvent e) {
                            updateTrainName();
                        }
                    }
            );

            book.addActionListener(e -> bookTicket());

            add(new JScrollPane(form), BorderLayout.CENTER);

            JLabel hint = new JLabel(
                    "Demo trains: 12001 Rajdhani | 12002 Shatabdi | "
                            + "12951 Mumbai Rajdhani",
                    SwingConstants.CENTER
            );
            add(hint, BorderLayout.SOUTH);
        }

        private String lookupTrainName(int number) {
            switch (number) {
                case 12001:
                    return "Rajdhani Express";
                case 12002:
                    return "Shatabdi Express";
                case 12951:
                    return "Mumbai Rajdhani";
                default:
                    return "Train " + number;
            }
        }

        private void updateTrainName() {
            try {
                int number = Integer.parseInt(
                        trainNumber.getText().trim()
                );

                if (number > 0) {
                    trainName.setText(lookupTrainName(number));
                } else {
                    trainName.setText("");
                }
            } catch (NumberFormatException ex) {
                trainName.setText("");
            }
        }

        private void bookTicket() {
            String name = passenger.getText().trim();
            String numberText = trainNumber.getText().trim();
            String dateText = journeyDate.getText().trim();
            String from = source.getText().trim();
            String to = destination.getText().trim();

            if (name.isEmpty() || numberText.isEmpty()
                    || dateText.isEmpty()
                    || from.isEmpty() || to.isEmpty()) {
                showError(this, "Please fill in all fields.");
                return;
            }

            int number;

            try {
                number = Integer.parseInt(numberText);
                if (number <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                showError(this, "Enter a valid positive train number.");
                return;
            }

            LocalDate date;

            try {
                date = LocalDate.parse(dateText);
            } catch (DateTimeParseException ex) {
                showError(this, "Date format must be YYYY-MM-DD.");
                return;
            }

            if (date.isBefore(LocalDate.now())) {
                showError(this, "Journey date cannot be in the past.");
                return;
            }

            if (from.equalsIgnoreCase(to)) {
                showError(
                        this,
                        "Source and destination must be different."
                );
                return;
            }

            String train = lookupTrainName(number);

            String pnr = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 10)
                    .toUpperCase();

            Reservation reservation = new Reservation(
                    pnr,
                    name,
                    number,
                    train,
                    (String) classType.getSelectedItem(),
                    date,
                    from,
                    to
            );

            reservations.add(reservation);

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation successful!\n\n" + reservation,
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            passenger.setText("");
            trainNumber.setText("");
            trainName.setText("");
            journeyDate.setText("");
            source.setText("");
            destination.setText("");
        }
    }

    // TICKET CANCELLATION
    private static class CancellationPanel extends JPanel {

        private final JTextField pnrField = field(18);
        private final JTextArea details = new JTextArea(10, 42);
        private Reservation fetchedReservation;

        private final JButton cancel =
                new JButton("Cancel This Ticket");

        CancellationPanel() {
            setLayout(new BorderLayout(8, 8));
            setBorder(new EmptyBorder(18, 18, 18, 18));

            JPanel top = new JPanel(
                    new FlowLayout(FlowLayout.LEFT)
            );

            top.add(new JLabel("Enter PNR:"));
            top.add(pnrField);

            JButton fetch = new JButton("Fetch Booking");
            top.add(fetch);

            add(top, BorderLayout.NORTH);

            details.setEditable(false);
            details.setFont(
                    new Font(Font.MONOSPACED, Font.PLAIN, 13)
            );

            add(new JScrollPane(details), BorderLayout.CENTER);

            cancel.setEnabled(false);

            JPanel bottom = new JPanel(
                    new FlowLayout(FlowLayout.RIGHT)
            );
            bottom.add(cancel);
            add(bottom, BorderLayout.SOUTH);

            fetch.addActionListener(e -> fetchBooking());
            pnrField.addActionListener(e -> fetch.doClick());
            cancel.addActionListener(e -> cancelBooking());
        }

        private void fetchBooking() {
            String pnr = pnrField.getText()
                    .trim()
                    .toUpperCase();

            if (pnr.isEmpty()) {
                showError(this, "Enter a PNR first.");
                return;
            }

            fetchedReservation = null;
            cancel.setEnabled(false);

            for (Reservation reservation : reservations) {
                if (reservation.pnr.equals(pnr)) {
                    fetchedReservation = reservation;
                    break;
                }
            }

            if (fetchedReservation == null) {
                details.setText(
                        "No booking found for PNR: " + pnr
                );
                return;
            }

            details.setText(
                    "BOOKING DETAILS\n"
                            + "------------------------------\n"
                            + fetchedReservation
            );

            cancel.setEnabled(true);
        }

        private void cancelBooking() {
            if (fetchedReservation == null) {
                showError(this, "Fetch a valid booking first.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Cancel ticket with PNR "
                            + fetchedReservation.pnr + "?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            reservations.remove(fetchedReservation);

            JOptionPane.showMessageDialog(
                    this,
                    "Ticket cancelled successfully."
            );

            details.setText("This booking has been cancelled.");
            pnrField.setText("");
            fetchedReservation = null;
            cancel.setEnabled(false);
        }
    }
}
