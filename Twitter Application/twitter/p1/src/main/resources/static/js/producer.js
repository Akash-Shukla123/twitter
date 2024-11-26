const messageContent = document.getElementById("message-content");
const sendMsgBtn = document.getElementById("sendmessage");
const token = localStorage.getItem("authToken"); // Assuming token is stored in localStorage

if (!token) {
    // If there's no token, redirect to login
    window.location.href = "/index.html";
}

sendMsgBtn.addEventListener("click", function (event) {
    event.preventDefault(); // Prevent default form submission
    const userData = {content: messageContent.value};

        if (!token) {
            alert("Token not found. Please log in again.");
            return;
        }

        // Step 1: Validate Token
        fetch("/auth/validate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`, // Send token in Authorization header
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Token validation failed.");
                }
                return response.json();
            })
            .then((validationResult) => {
                if (validationResult.valid) {
                    // Step 2: Proceed with User Creation
                    return fetch("/messages/send", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`, // Include token for secure endpoints
                        },
                        body: JSON.stringify(userData),
                    });
                } else {
                    throw new Error("Invalid token.");
                }
            })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to send message.");
                }
                return response.json();
            })
            .then((data) => {
                alert("Message Sent successfully!");
                console.log("Message data:", data);
                messageContent.value = ""; // Reset the form after success
            })
            .catch((error) => {
                console.error("Error:", error);
                alert(error.message || "An error occurred.");
            });
});