function validateToken(token){

    return fetch("/auth/validate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: token }),
    })
        .then((response) => {
            if(response.ok){
                return true;
            }else{
                console.log("Invalid Token. You don't have access to this page.");
                //window.location.href = "/index.html";
                return false;
            }
        })
        .then((data) => {
            console.log("user validated");
            return true;
        })
        .catch((error) => {
            console.error("Error:", error);
            alert("Failed to validate user.");
            return false;
        });
}