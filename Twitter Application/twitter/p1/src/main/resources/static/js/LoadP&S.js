// Function to validate the token
async function validateTokenForProducer() {
    const token = localStorage.getItem('authToken'); // Replace 'authToken' with your token key
    if (!token) {
        console.error('No token found');
        redirectToLogin();
        return false;
    }

    try {
        const response = await fetch('/auth/validate', {
            method: 'POST', // or 'GET' depending on your API
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Include token in headers
            },
            body: JSON.stringify({ token }) // Optional, depending on API needs
        });

        if (response.ok) {
            console.log('Token is valid');
            return true;
        } else {
            console.error('Invalid token');
            redirectToLogin();
            return false;
        }
    } catch (error) {
        console.error('Error validating token:', error);
        redirectToLogin();
        return false;
    }
}

// Redirect to login page if token is invalid
function redirectToLogin() {
    alert('Your session has expired. Redirecting to login.');
    window.location.href = '/index.html'; // Replace with your login page URL
}

// Fetch producers if the token is valid
async function initPage() {
    const isValid = await validateTokenForProducer();
    if (isValid) {
        fetchProducers();
    }
}

// Fetch producers function (same as before)
async function fetchProducers() {
    try {
        const response = await fetch('/users/read-producers');
        if (!response.ok) {
            throw new Error('Failed to fetch producers');
        }
        const producers = await response.json();

        const producersList = document.getElementById('producers');
        producersList.innerHTML = '';

        producers.forEach(producer => {
            const producerItem = document.createElement('div');
            producerItem.classList.add('producer-item');

            const producerName = document.createElement('span');
            producerName.classList.add('producer-name');
            producerName.textContent = producer;

            const subscribeButton = document.createElement('button');
            subscribeButton.classList.add('subscribe-btn');
            subscribeButton.textContent = 'Subscribe';
            subscribeButton.onclick = () => subscribeToProducer(producer);

            producerItem.appendChild(producerName);
            producerItem.appendChild(subscribeButton);

            producersList.appendChild(producerItem);
        });
    } catch (error) {
        console.error('Error fetching producers:', error);
    }
}

// Subscribe to a producer
function subscribeToProducer(producerId) {

    const token = localStorage.getItem('authToken');
    const userData = {producername: producerId};

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
                        return fetch("/users/create-subscription", {
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
                        throw new Error("Failed to subscribe.");
                    }
                    return response.json();
                })
                .then((data) => {
                    alert(`Subscribed to Producer ID: ${producerId}`);
                })
                .catch((error) => {
                    console.error("Error:", error);
                    alert(error.message || "An error occurred.");
                });
}

// Initialize the page
document.addEventListener('DOMContentLoaded', initPage);