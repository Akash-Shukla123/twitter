const toggleLink = document.getElementById("toggleLink");
const toggleText = document.getElementById("toggleText");
const formTitle = document.getElementById("formTitle");
const loginFields = document.getElementById("loginFields");
const registerFields = document.getElementById("registerFields");
const submitButton = document.getElementById("submitButton");
const form = document.getElementById("form");

// Function to toggle required attributes and enable/disable inputs
function toggleRequired(container, required, isVisible) {
    const inputs = container.querySelectorAll("input, select");
    inputs.forEach((input) => {
        if (required) {
            input.setAttribute("required", "true");
        } else {
            input.removeAttribute("required");
        }
        input.disabled = !required; // Disable inputs when not required
    });

    // Ensure the container is shown or hidden appropriately
    container.style.display = isVisible ? "block" : "none";
}


toggleLink.addEventListener("click", (e) => {
    e.preventDefault();

    if (loginFields.style.display === "none") {
        // Switching to Login mode
        formTitle.textContent = "Login";
        loginFields.style.display = "block";
        registerFields.style.display = "none";
        submitButton.textContent = "Login";
        toggleText.textContent = "Don't have an account?";
        toggleLink.textContent = "Register";

        toggleRequired(loginFields, true, true); // Enable login inputs
        toggleRequired(registerFields, false, false); // Disable register inputs
    } else {
        // Switching to Register mode
        formTitle.textContent = "Register";
        loginFields.style.display = "none";
        registerFields.style.display = "block";
        submitButton.textContent = "Register";
        toggleText.textContent = "Already have an account?";
        toggleLink.textContent = "Login";

        toggleRequired(loginFields, false, false); // Disable login inputs
        toggleRequired(registerFields, true, true); // Enable register inputs
    }
});

form.addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    if (loginFields.style.display !== "none") {
        // Login mode logic
        const userData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
        };

        fetch("/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Login failed");
                }
                return response.json();
            })
            .then((data) => {
                if (data.token && data.redirectUrl) {

                    alert("Login successfully!");
                    console.log("Login:", data);
                    form.reset(); // Reset the form after success

                    // Store the token
                    localStorage.setItem("authToken", data.token);

                    // Redirect to the URL sent from the backend
                    window.location.href = data.redirectUrl;
                } else {
                    alert("Invalid response from server");
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("Failed to login user.");
            });
    } else {
        // Ensure registration fields are visible
        if (registerFields.style.display === "none") {
            alert("Please switch to the register form before submitting.");
            return;
        }

        // Register mode logic
        const selectedRoles = Array.from(document.getElementById("roles").selectedOptions).map(
                (option) => ({ rolename: option.value })
            );

        const userData = {
            username: document.getElementById("registerUsername").value,
            email: document.getElementById("email").value,
            password: document.getElementById("registerPassword").value,
            roles: selectedRoles,
        };

        fetch("/users/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error("Failed to create user.");
            }
            return response.json();
        })
            .then((data) => {
                alert("User created successfully!");
                form.reset(); // Reset the form after success
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("Failed to create user.");
            });
    }
});
