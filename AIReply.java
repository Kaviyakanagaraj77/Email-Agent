import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AIReply {

    // Your OpenAI API key (keep it all on one line)
private static final String API_KEY = System.getenv("IU6kIOdvnBKz_0ephnzEq9VI3eaOJyFTcozT-px7oPpadKMhXY92RxyS0kbpMc7CLWSFp_-DkVT3BlbkFJ84lC7UE3N4z4ot3JanXrGoRWVicgmljy4M2ZkJYuMuUlwgbS_Fu9_cLdeLVZj8JhTY9e3K8lMA");

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String LOG_FILE = "replies.txt";

    public static String generateReply(String emailText) {
        String lowerEmail = emailText.toLowerCase();
        String reply = "";

        // ----------------------
        // RULE-BASED RESPONSES
        // ----------------------
        if (lowerEmail.contains("admission")) {
            reply = "Thank you for contacting us regarding college admissions. Please visit our official website for full details.";
        } 
        else if (lowerEmail.contains("fee") || lowerEmail.contains("fees")) {
            reply = "Our fee structure depends on the course. Kindly check the brochure.";
        } 
        else if (lowerEmail.contains("hostel")) {
            reply = "Yes, hostel facilities are available for students.";
        } 
        else if (lowerEmail.contains("scholarship")) {
            reply = "We offer scholarships based on merit and need. Please check our scholarship page.";
        } 
        else if (lowerEmail.contains("placement") || lowerEmail.contains("job")) {
            reply = "Our placement cell provides guidance and opportunities for students. Visit the placement section on our website.";
        } 
        else if (lowerEmail.contains("contact") || lowerEmail.contains("phone") || lowerEmail.contains("email")) {
            reply = "You can contact us at our official email or phone number listed on our website.";
        } 
        else if (lowerEmail.contains("courses") || lowerEmail.contains("programs")) {
            reply = "We offer a variety of courses. Please visit our courses page for more details.";
        } 
        else if (lowerEmail.contains("events") || lowerEmail.contains("seminar")) {
            reply = "We organize regular events and seminars. Check our events calendar on the website.";
        } 
        else if (lowerEmail.contains("library")) {
            reply = "Our library has a wide collection of books and journals.";
        } 
        else if (lowerEmail.contains("sports")) {
            reply = "We have facilities for various sports and regular tournaments.";
        } 
        else if (lowerEmail.contains("history")) {
            reply = "Our college was established in 1990 and has a rich history of academic excellence.";
        }
        // ----------------------
        // FALLBACK: OpenAI API
        // ----------------------
        else {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{"
                        + "\"model\": \"gpt-3.5-turbo\","
                        + "\"messages\": [{\"role\": \"user\", \"content\": \"" + emailText + "\"}]"
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonInputString.getBytes("utf-8"));
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }

                String resp = response.toString();
                int index = resp.indexOf("\"content\":\"");
                if (index != -1) {
                    int start = index + 10;
                    int end = resp.indexOf("\"", start);
                    if (end != -1) {
                        reply = resp.substring(start, end);
                        reply = reply.replace("\\n", "\n").replace("\\\"", "\"");
                    }
                }
            } catch (Exception e) {
                reply = "Thank you for your email. We will get back to you soon.";
            }
        }

        // ----------------------
        // FORMAT PROFESSIONAL EMAIL
        // ----------------------
        String finalReply = formatReply(reply);

        // ----------------------
        // LOG EMAIL & REPLY TO FILE
        // ----------------------
        logEmail(emailText, finalReply);

        return finalReply;
    }

    // ----------------------
    // Helper: Professional Email Formatting
    // ----------------------
    private static String formatReply(String body) {
        return "Subject: Response to Your Inquiry\n\n" +
               "Dear Student,\n\n" +
               body + "\n\n" +
               "Best regards,\n" +
               "Admissions Team";
    }

    // ----------------------
    // Helper: Log Emails & Replies
    // ----------------------
    private static void logEmail(String email, String reply) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.println("[" + timestamp + "] Email received: " + email);
            out.println("[" + timestamp + "] AI Reply:\n" + reply);
            out.println("-----------------------------------------------------");

        } catch (IOException e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }
}
