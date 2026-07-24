📱 Secret Vault – Android Calculator App
Secret Vault is a cleverly disguised Android application that seamlessly combines a fully functional calculator with a hidden, password‑protected media vault. On the surface, it works like any everyday calculator, but with a secret keystroke sequence, it transforms into a secure gallery for storing private images.

🎯 Overview
Built with Kotlin and following Android best practices, this app demonstrates a dual‑purpose design:

Front‑end – a reliable calculator with basic arithmetic, parentheses, and percentage operations.

Back‑end – a stealthy vault that can be unlocked by entering a custom password and tapping the equals (=) button.

Once inside, users can import photos from their device, view them in a gallery grid, or open any image in full‑screen mode. The app also includes a hidden settings trigger for password management, making it both secure and user‑friendly.

✨ Key Features
🔢 Fully Functional Calculator
Supports addition, subtraction, multiplication, division, brackets, and percentage calculations.

Employs a safe, in‑built expression evaluator to handle complex arithmetic without security risks.

🕵️ Disguised Authentication
The app looks and behaves like a standard calculator.

Enter a predefined password (set during initial setup) and press = to unlock the vault – the calculator interface instantly reveals the hidden gallery.

📂 Secure Media Import
Uses Android’s ACTION_OPEN_DOCUMENT intent to let users pick multiple images from their device.

Files are stored exclusively in the app’s private internal storage (filesDir), ensuring they remain inaccessible to other apps.

🖼️ Custom Gallery & Viewer
Displays imported images in a scrollable grid (GridLayout) for quick browsing.

Tap any thumbnail to open the image in a full‑screen viewer with pinch‑to‑zoom support (optional enhancement).

⚙️ Password Management (Hidden Trigger)
A 5‑tap gesture on the settings icon (gear) launches a secure dialog to reset or update the vault password.

The new password is saved persistently using SharedPreferences.

🛠️ Tech Stack & Architecture
Component	Technology / Approach
Language	Kotlin
UI Layouts	XML (RelativeLayout, LinearLayout, GridLayout)
Persistence	SharedPreferences (passwords) & Internal Storage (images)
Android APIs	Intents, ContentResolver, AlertDialog, File I/O
Min SDK	API 24+ (recommended)
🧩 App Structure (Activities)
MainActivity.kt – Handles calculator logic, button clicks, expression evaluation, and intercepts the password sequence.

VaultActivity.kt – Manages image selection via the system document picker and securely saves files to internal storage.

GalleryActivity.kt – Reads saved images from internal storage and populates a dynamic grid.

FullScreenActivity.kt – Displays a selected image in full‑screen mode with smooth transitions.

🚀 How to Run the Project
Clone or download this repository.

Open Android Studio and select Open an Existing Project.

Wait for Gradle to sync (if prompted, accept any updates).

Connect a physical Android device or start an emulator (API 24+ recommended).

Click the Run ▶ button to build and install the app.

💡 How It Works – Under the Hood
Calculator Mode:
All arithmetic is handled by a safe ExpressionEvaluator that parses and computes the input string, returning the result in real time.

Vault Unlock:
When the user types a sequence of digits followed by =, the app compares the entered string against the stored password. If they match, an explicit intent launches VaultActivity.

File Security:
Images are saved with unique filenames (timestamp‑based) inside filesDir. The app does not request READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permissions, relying solely on the document picker for input and internal storage for output – this aligns with modern Android scoped storage guidelines.

Password Reset:
A 5‑tap gesture on the settings icon triggers a dialog where the user must confirm the current password and then set a new one, ensuring only authorised users can change credentials.

🔮 Future Enhancements (Ideas)
Add video support alongside images.

Implement PIN/password encryption for saved files.

Introduce biometric authentication (fingerprint/face unlock) as an alternative vault entry.

Allow users to organise images into albums or folders.

Enable image deletion directly from the gallery.

📄 License
This project is open‑source and available under the MIT License.

👨‍💻 Contribution
Pull requests and feature suggestions are welcome! For major changes, please open an issue first to discuss what you’d like to improve.

📬 Contact
If you have any questions or feedback, feel free to reach out via [email] or open an issue on GitHub.

I hope this version presents your project in a clear, engaging, and professional light. Let me know if you’d like a Hindi translation as well – I’d be happy to provide that too! 😊
