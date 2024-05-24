document.addEventListener('DOMContentLoaded', () => {
    const allUsersBtn = document.getElementById('allUsersBtn');
    const employeesBtn = document.getElementById('employeesBtn');
    const managersBtn = document.getElementById('managersBtn');
    const projectsBtn = document.getElementById('projectsBtn');
    const requestsBtn = document.getElementById('requestsBtn');
    const changePasswordBtn = document.getElementById('changePasswordBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const mainContent = document.getElementById('mainContent');

    // Event listeners for buttons
    allUsersBtn.addEventListener('click', loadAllUsers);
    employeesBtn.addEventListener('click', loadEmployees);
    managersBtn.addEventListener('click', loadManagers);
    projectsBtn.addEventListener('click', loadProjects);
    requestsBtn.addEventListener('click', loadRequests);
    changePasswordBtn.addEventListener('click', changePassword);
    logoutBtn.addEventListener('click', logout);

    // Load all users function
    async function loadAllUsers() {
        mainContent.innerHTML = '<h2>All Users</h2>';
        // Fetch all users from the backend
        const users = await makeAuthenticatedRequest('http://localhost:8080/user/get');
        if (!users) {
            console.error('Failed to load users');
            return;
        }

        const usersTable = document.createElement('table');
        usersTable.innerHTML = `
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Contact Number</th>
                    <th>Status</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                ${users.map(user => `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.contactNumber}</td>
                        <td>
                            <input type="checkbox" ${user.status === 'true' ? 'checked' : ''} 
                            data-user-id="${user.id}" class="status-toggle">
                        </td>
                        <td>${user.role}</td>
                        <td>
                            <button class="update-btn" data-user-id="${user.id}">Update</button>
                            <button class="delete-btn" data-user-id="${user.id}">Delete</button>
                        </td>
                    </tr>`).join('')}
            </tbody>
        `;
        mainContent.appendChild(usersTable);

        // Event listeners for status toggle, update, and delete buttons
        document.querySelectorAll('.status-toggle').forEach(toggle => {
            toggle.addEventListener('change', async (event) => {
                const userId = event.target.dataset.userId;
                const status = event.target.checked ? 'true' : 'false';

                try {
                    const response = await makeAuthenticatedRequest('http://localhost:8080/user/updateStatus', {
                        method: 'POST',
                        body: JSON.stringify({ id: userId, status })
                    });

                    if (response) {
                        console.log('User status updated successfully:', response);
                    } else {
                        console.error('Failed to update status');
                    }
                } catch (error) {
                    console.error('Error during status update:', error);
                    alert('An error occurred. Please try again.');
                }
            });
        });

        document.querySelectorAll('.update-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                const userId = event.target.dataset.userId;
                showUpdateForm(userId);
            });
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', async (event) => {
                const id = event.target.dataset.id;
                console.log(`Attempting to delete user ID: ${id}`);
                const response = await makeAuthenticatedRequest(`http://localhost:8080/user/delete/${id}`, {
                    method: 'DELETE'
                });
                if (response) {
                    loadAllUsers(); // Refresh the list
                } else {
                    console.error('Failed to delete user');
                }
            });
        });
    }

    // Function to show update form
    function showUpdateForm(userId) {
        mainContent.innerHTML = `
            <h2>Update User Details</h2>
            <form id="updateUserForm">
                <label for="updateName">Name:</label>
                <input type="text" id="updateName" name="name">
                <label for="updateEmail">Email:</label>
                <input type="email" id="updateEmail" name="email">
                <label for="updateContactNumber">Contact Number:</label>
                <input type="text" id="updateContactNumber" name="contactNumber">
                <button type="submit">Update</button>
                <button type="button" id="backBtn">Back</button>
            </form>
        `;

        const updateUserForm = document.getElementById('updateUserForm');
        const backBtn = document.getElementById('backBtn');

        backBtn.addEventListener('click', loadAllUsers);

        updateUserForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const updatedDetails = { id: id };

            if (updateUserForm.updateName.value) updatedDetails.name = updateUserForm.updateName.value;
            if (updateUserForm.updateEmail.value) updatedDetails.email = updateUserForm.updateEmail.value;
            if (updateUserForm.updateContactNumber.value) updatedDetails.contactNumber = updateUserForm.updateContactNumber.value;

            try {
                const response = await makeAuthenticatedRequest('http://localhost:8080/user/update-details', {
                    method: 'PUT',
                    body: JSON.stringify(updatedDetails)
                });

                if (response) {
                    console.log('User details updated successfully:', response);
                    loadAllUsers();
                } else {
                    console.error('Failed to update user details');
                }
            } catch (error) {
                console.error('Error during user update:', error);
                alert('An error occurred. Please try again.');
            }
        });
    }

    // Load employees function
    function loadEmployees() {
        mainContent.innerHTML = '<h2>Employees</h2>';
        // Implement functionality to load employees
    }

    // Load managers function
    function loadManagers() {
        mainContent.innerHTML = '<h2>Managers</h2>';
        // Implement functionality to load managers
    }

    // Load projects function
    function loadProjects() {
        mainContent.innerHTML = '<h2>Projects</h2>';
        // Implement functionality to load projects
    }

    // Load requests function
    function loadRequests() {
        mainContent.innerHTML = '<h2>Requests</h2>';
        // Implement functionality to load requests
    }

    // Change password function
    function changePassword() {
        mainContent.innerHTML = '<h2>Change Password</h2>';
        // Implement change password functionality
    }

    // Logout function
    function logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = 'landingpage.html';
    }

    // Function to make authenticated requests
    async function makeAuthenticatedRequest(url, options = {}) {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('You need to log in first.');
            return;
        }

        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        try {
            const response = await fetch(url, options);
            if (response.ok) {
                return await response.json();
            } else {
                const errorData = await response.json();
                console.error('Request failed:', errorData.message);
                alert(errorData.message || 'Request failed. Please try again.');
            }
        } catch (error) {
            console.error('Error during request:', error);
            alert('An error occurred. Please try again.');
        }
    }

    // Initially load all users
    loadAllUsers();
});
