// Validate the token
async function validateToken() {
    const token = localStorage.getItem("authToken"); // Replace 'authToken' with your token key
    if (!token) {
        console.error("No token found");
        redirectToLogin();
        return false;
    }

    try {
        const response = await fetch("/auth/validate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`, // Include token in headers
            },
            body: JSON.stringify({ token }), // Optional, depending on API requirements
        });

        if (response.ok) {
            console.log("Token is valid");
            return true;
        } else {
            console.error("Invalid token");
            redirectToLogin();
            return false;
        }
    } catch (error) {
        console.error("Error validating token:", error);
        redirectToLogin();
        return false;
    }
}

// Redirect to login page if token is invalid
function redirectToLogin() {
    alert("Your session has expired. Redirecting to login.");
    window.location.href = "/index.html"; // Replace with your login page URL
}

// Fetch messages dynamically
async function fetchMessages() {
    const token = localStorage.getItem("authToken");

    try {
        const response = await fetch("/messages/getmessageForSubs", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error("Failed to fetch messages");
        }

        const messages = await response.json();
        const messageFeed = document.querySelector(".message-feed");
        messageFeed.innerHTML = ""; // Clear any existing messages

        messages.forEach(message => {
            const messageItem = document.createElement("div");
            messageItem.classList.add("message-item");

            const messageContent = document.createElement("p");
            messageContent.classList.add("message-content");
            messageContent.textContent = message.content; // Assuming message.content contains the text of the message

            const messageSender = document.createElement("p");
            messageSender.classList.add("message-sender");
            messageSender.textContent = `- ${message.sender}`; // Assuming message.sender contains the producer name

            messageItem.appendChild(messageContent);
            messageItem.appendChild(messageSender);
            messageFeed.appendChild(messageItem);
        });
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
}

// Initialize the page
async function initPageMessage() {
    const isValid = await validateToken();
    if (isValid) {
        fetchMessages();
    }
}

document.addEventListener("DOMContentLoaded", initPageMessage);