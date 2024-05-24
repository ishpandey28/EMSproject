document.addEventListener('DOMContentLoaded', () => {
    const loginBtn = document.getElementById('loginBtn');
    const signupBtn = document.getElementById('signupBtn');
    const forgotPasswordBtn = document.getElementById('forgotPasswordBtn');

    const loginModal = document.getElementById('loginModal');
    const signupModal = document.getElementById('signupModal');
    const forgotPasswordModal = document.getElementById('forgotPasswordModal');

    const closeLogin = document.getElementById('closeLogin');
    const closeSignup = document.getElementById('closeSignup');
    const closeForgotPassword = document.getElementById('closeForgotPassword');

    loginBtn.onclick = () => loginModal.style.display = 'block';
    signupBtn.onclick = () => signupModal.style.display = 'block';
    forgotPasswordBtn.onclick = () => forgotPasswordModal.style.display = 'block';

    closeLogin.onclick = () => loginModal.style.display = 'none';
    closeSignup.onclick = () => signupModal.style.display = 'none';
    closeForgotPassword.onclick = () => forgotPasswordModal.style.display = 'none';

    window.onclick = (event) => {
        if (event.target == loginModal) loginModal.style.display = 'none';
        if (event.target == signupModal) signupModal.style.display = 'none';
        if (event.target == forgotPasswordModal) forgotPasswordModal.style.display = 'none';
    };

    // Store JWT token
    let jwtToken = '';

    // Login form submission
    document.getElementById('loginForm').onsubmit = async (event) => {
        event.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await fetch('http://localhost:8080/user/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                jwtToken = data.token; // Store the token in a variable or localStorage/sessionStorage
                localStorage.setItem('jwtToken', jwtToken);

                // Decode JWT to get role (you may need a JWT decoding library or decode it on the backend)
                const payload = JSON.parse(atob(jwtToken.split('.')[1]));
                const userRole = payload.role;

                if (userRole === 'admin') {
                    window.location.href = 'adminDashboard.html';
                } else if (userRole === 'manager') {
                    window.location.href = 'managerDashboard.html';
                } else {
                    window.location.href = 'employeeDashboard.html';
                }
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Login failed. Please check your credentials.');
            }
        } catch (error) {
            console.error('Error during login:', error);
            alert('An error occurred. Please try again.');
        }
    };

    // Signup form submission
    document.getElementById('signupForm').onsubmit = async (event) => {
        event.preventDefault();
        const name = document.getElementById('signupName').value;
        const contactNumber = document.getElementById('signupContact').value;
        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const role = document.getElementById('signupRole').value;

        try {
            const response = await fetch('http://localhost:8080/user/signup', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, contactNumber, email, password, role })
            });

            if (response.ok) {
                alert('Signup successful. Please wait for admin approval.');
                signupModal.style.display = 'none';
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Signup failed. Please try again.');
            }
        } catch (error) {
            console.error('Error during signup:', error);
            alert('An error occurred. Please try again.');
        }
    };

    // Forgot password form submission
    document.getElementById('forgotPasswordForm').onsubmit = async (event) => {
        event.preventDefault();
        const email = document.getElementById('forgotPasswordEmail').value;

        try {
            const response = await fetch('http://localhost:8080/user/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email })
            });

            if (response.ok) {
                alert('Please check your email for password reset instructions.');
                forgotPasswordModal.style.display = 'none';
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Failed to send reset instructions. Please try again.');
            }
        } catch (error) {
            console.error('Error during password reset:', error);
            alert('An error occurred. Please try again.');
        }
    };

    // Function to make authenticated requests
    async function makeAuthenticatedRequest(url, options = {}) {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('You need to log in first.');
            return;
        }

        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };

        try {
            const response = await fetch(url, options);
            if (response.ok) {
                return await response.json();
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Request failed. Please try again.');
            }
        } catch (error) {
            console.error('Error during request:', error);
            alert('An error occurred. Please try again.');
        }
    }
});
