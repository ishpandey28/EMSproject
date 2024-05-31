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
                const id = event.target.dataset.userId;
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
            <form id="updateUserForm" class="center-form">
                <label for="updateName">Name:</label>
                <input type="text" id="updateName" name="name">
                <label for="updateEmail">Email:</label>
                <input type="email" id="updateEmail" name="email">
                <label for="updateContactNumber">Contact Number:</label>
                <input type="text" id="updateContactNumber" name="contactNumber">
                <div class="button-group">
                    <button type="submit">Update</button>
                    <button type="button" id="backBtn">Back</button>
                </div>
            </form>
        `;

        const updateUserForm = document.getElementById('updateUserForm');
        const backBtn = document.getElementById('backBtn');

        backBtn.addEventListener('click', loadAllUsers);

        updateUserForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const updatedDetails = { id: userId };

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
            }e
        });
    }

    // Load employees function
    async function loadEmployees() {
        mainContent.innerHTML = `
            <h2>Employees</h2>
            <input type="text" id="employeeSearch" placeholder="Enter Employee ID">
            <button id="searchEmployeeBtn">Search</button>
            <div id="employeeDetails"></div>
            <table id="employeesTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Contact Number</th>
                        <th>Email</th>
                        <th>Designation</th>
                        <th>Project Status</th>
                        <th>Assigned</th>
                        <th>Manager Name</th>
                        <th>Project Name</th>
                        <th>Skills</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        `;

        const searchEmployeeBtn = document.getElementById('searchEmployeeBtn');
        const employeeSearch = document.getElementById('employeeSearch');
        const employeeDetails = document.getElementById('employeeDetails');
        const employeesTableBody = document.querySelector('#employeesTable tbody');

        // Fetch and display all employees
        const employees = await makeAuthenticatedRequest('http://localhost:8080/employee/getAll');
        if (employees) {
            employeesTableBody.innerHTML = employees.map(employee => `
                <tr>
                    <td>${employee.empId}</td>
                    <td>${employee.name}</td>
                    <td>${employee.contactNumber}</td>
                    <td>${employee.email}</td>
                    <td>${employee.designation}</td>
                    <td>${employee.projectStatus}</td>
                    <td>${employee.assigned}</td>
                    <td>${employee.managerName}</td>
                    <td>${employee.projectName}</td>
                    <td>${Array.from(employee.skills).join(', ')}</td>
                </tr>
            `).join('');
        } else {
            employeesTableBody.innerHTML = '<tr><td colspan="10">No employees found</td></tr>';
        }

        // Search employee by ID
        searchEmployeeBtn.addEventListener('click', async () => {
            const employeeId = employeeSearch.value.trim();
            if (employeeId) {
                const employee = await makeAuthenticatedRequest(`http://localhost:8080/employee/find/${employeeId}`);
                if (employee) {
                    employeeDetails.innerHTML = `
                        <h3>Employee Details</h3>
                        <p>ID: ${employee.empId}</p>
                        <p>Name: ${employee.name}</p>
                        <p>Contact Number: ${employee.contactNumber}</p>
                        <p>Email: ${employee.email}</p>
                        <p>Designation: ${employee.designation}</p>
                        <p>Project Status: ${employee.projectStatus}</p>
                        <p>Assigned: ${employee.assigned}</p>
                        <p>Manager Name: ${employee.managerName}</p>
                        <p>Project Name: ${employee.projectName}</p>
                        <p>Skills: ${Array.from(employee.skills).join(', ')}</p>
                    `;
                } else {
                    employeeDetails.innerHTML = '<p>No employee found with the given ID</p>';
                }
            } else {
                alert('Please enter an employee ID');
            }
        });
    }

    // Load managers function
    async function loadManagers() {
        mainContent.innerHTML = `
            <h2>Managers</h2>
            <input type="text" id="managerSearch" placeholder="Enter Manager ID">
            <button id="searchManagerBtn">Search</button>
            <div id="managerDetails"></div>
            <table id="managersTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Contact Number</th>
                        <th>Email</th>
                        <th>Designation</th>
                        <th>Employees</th>
                        <th>Project Names</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        `;
    
        const searchManagerBtn = document.getElementById('searchManagerBtn');
        const managerSearch = document.getElementById('managerSearch');
        const managerDetails = document.getElementById('managerDetails');
        const managersTableBody = document.querySelector('#managersTable tbody');
    
        // Fetch and display all managers
        const managers = await makeAuthenticatedRequest('http://localhost:8080/manager/getAll');
        if (managers) {
            managersTableBody.innerHTML = managers.map(manager => `
                <tr>
                    <td>${manager.mgrId}</td>
                    <td>${manager.name}</td>
                    <td>${manager.contactNumber}</td>
                    <td>${manager.email}</td>
                    <td>${manager.designation}</td>
                    <td>${Array.from(manager.employees).join(', ')}</td>
                    <td>${Array.from(manager.projectNames).join(', ')}</td>
                </tr>
            `).join('');
        } else {
            managersTableBody.innerHTML = '<tr><td colspan="7">No managers found</td></tr>';
        }
    
        // Search manager by ID
        searchManagerBtn.addEventListener('click', async () => {
            const managerId = managerSearch.value.trim();
            if (managerId) {
                const manager = await makeAuthenticatedRequest(`http://localhost:8080/manager/find/${managerId}`);
                if (manager) {
                    managerDetails.innerHTML = `
                        <h3>Manager Details</h3>
                        <p>ID: ${manager.mgrId}</p>
                        <p>Name: ${manager.name}</p>
                        <p>Contact Number: ${manager.contactNumber}</p>
                        <p>Email: ${manager.email}</p>
                        <p>Designation: ${manager.designation}</p>
                        <p>Employees: ${Array.from(manager.employees).join(', ')}</p>
                        <p>Project Names: ${Array.from(manager.projectNames).join(', ')}</p>
                    `;
                } else {
                    managerDetails.innerHTML = '<p>No manager found with the given ID</p>';
                }
            } else {
                alert('Please enter a manager ID');
            }
        });
    }
    async function loadProjects() {
        mainContent.innerHTML = `
            <h2>Projects</h2>
            <button id="addProjectBtn">Add Project</button>
            <button id="assignManagerBtn">Assign Manager</button>
            <input type="text" id="projectSearch" placeholder="Enter Project ID">
            <button id="searchProjectBtn">Search</button>
            <div id="projectDetails"></div>
            <table id="projectsTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
            <div id="assignManagerForm" style="display:none;">
                <h3>Assign Manager to Project</h3>
                <form>
                    <label for="assignProjectId">Project ID:</label>
                    <input type="text" id="assignProjectId"><br>
                    <label for="assignManagerId">Manager ID:</label>
                    <input type="text" id="assignManagerId"><br>
                    <button type="button" id="submitAssignmentBtn">Assign</button>
                    <button type="button" id="backBtn">Back</button>
                </form>
            </div>
            <div id="addProjectForm" style="display:none;">
                <h3>Add Project</h3>
                <form>
                    <label for="newProjectName">Project Name:</label>
                    <input type="text" id="newProjectName"><br>
                    <button type="button" id="submitNewProjectBtn">Add Project</button>
                    <button type="button" id="cancelAddProjectBtn">Cancel</button>
                </form>
            </div>
        `;
    
        const addProjectBtn = document.getElementById('addProjectBtn');
        const assignManagerBtn = document.getElementById('assignManagerBtn');
        const searchProjectBtn = document.getElementById('searchProjectBtn');
        const projectSearch = document.getElementById('projectSearch');
        const projectDetails = document.getElementById('projectDetails');
        const projectsTableBody = document.querySelector('#projectsTable tbody');
        const assignManagerForm = document.getElementById('assignManagerForm');
        const submitAssignmentBtn = document.getElementById('submitAssignmentBtn');
        const backBtn = document.getElementById('backBtn');
        const addProjectForm = document.getElementById('addProjectForm');
        const submitNewProjectBtn = document.getElementById('submitNewProjectBtn');
        const cancelAddProjectBtn = document.getElementById('cancelAddProjectBtn');
    
        // Fetch and display all projects
        const projects = await makeAuthenticatedRequest('http://localhost:8080/projects/all');
        if (projects) {
            projectsTableBody.innerHTML = projects.map(project => `
                <tr>
                    <td>${project.projectId}</td>
                    <td>${project.projectName}</td>
                    <td>
                        <button class="update-btn" data-project-id="${project.projectId}">Update</button>
                        <button class="delete-btn" data-project-id="${project.projectId}">Delete</button>
                    </td>
                </tr>
            `).join('');
        } else {
            projectsTableBody.innerHTML = '<tr><td colspan="3">No projects found</td></tr>';
        }
    
        // Event delegation for update and delete buttons
        projectsTableBody.addEventListener('click', (event) => {
            if (event.target.classList.contains('update-btn')) {
                const projectId = event.target.getAttribute('data-project-id');
                updateProject(projectId);
            } else if (event.target.classList.contains('delete-btn')) {
                const projectId = event.target.getAttribute('data-project-id');
                deleteProject(projectId);
            }
        });
    
        // Show the form to assign a manager to a project
        assignManagerBtn.addEventListener('click', () => {
            assignManagerForm.style.display = 'block';
            addProjectForm.style.display = 'none';
            projectsTableBody.parentElement.style.display = 'none';
        });
    
        // Show the form to add a new project
        addProjectBtn.addEventListener('click', () => {
            addProjectForm.style.display = 'block';
            assignManagerForm.style.display = 'none';
            projectsTableBody.parentElement.style.display = 'none';
        });
    
        // Hide the form and show the project list again
        backBtn.addEventListener('click', () => {
            assignManagerForm.style.display = 'none';
            addProjectForm.style.display = 'none';
            projectsTableBody.parentElement.style.display = 'block';
        });
    
        cancelAddProjectBtn.addEventListener('click', () => {
            addProjectForm.style.display = 'none';
            assignManagerForm.style.display = 'none';
            projectsTableBody.parentElement.style.display = 'block';
        });
    
        // Submit the assignment of a manager to a project
        submitAssignmentBtn.addEventListener('click', async () => {
            const projectId = document.getElementById('assignProjectId').value.trim();
            const managerId = document.getElementById('assignManagerId').value.trim();
            if (projectId && managerId) {
                const response = await makeAuthenticatedRequest('http://localhost:8080/projects/assignProjectToManager', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ projectId: parseInt(projectId), mgrId: parseInt(managerId) })
                });
                if (response) {
                    alert('Manager assigned to project successfully');
                    loadProjects();
                } else {
                    alert('Failed to assign manager to project');
                }
            } else {
                alert('Please enter both project ID and manager ID');
            }
        });
    
        // Submit the new project
        submitNewProjectBtn.addEventListener('click', async () => {
            const projectName = document.getElementById('newProjectName').value.trim();
            if (projectName) {
                const response = await makeAuthenticatedRequest('http://localhost:8080/projects/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ projectName: projectName })
                });
                if (response) {
                    alert('Project added successfully');
                    loadProjects();
                } else {
                    alert('Failed to add project');
                }
            } else {
                alert('Please enter a project name');
            }
        });
    
        // Search project by ID
        searchProjectBtn.addEventListener('click', async () => {
            const projectId = projectSearch.value.trim();
            if (projectId) {
                const project = await makeAuthenticatedRequest('http://localhost:8080/projects/getById', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ projectId: parseInt(projectId) })
                });
                if (project) {
                    projectDetails.innerHTML = `
                        <h3>Project Details</h3>
                        <p>ID: ${project.projectId}</p>
                        <p>Name: ${project.projectName}</p>
                    `;
                } else {
                    projectDetails.innerHTML = '<p>No project found with the given ID</p>';
                }
            } else {
                alert('Please enter a project ID');
            }
        });
    }
    async function updateProject(projectId) {
        const newName = prompt('Enter new project name:');
        if (newName) {
            const response = await makeAuthenticatedRequest('http://localhost:8080/projects/update', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ projectId: projectId.toString(), projectName: newName })
            });
            if (response) {
                alert('Project updated successfully');
                loadProjects();
            } else {
                alert('Failed to update project');
            }
        }
    }
    
    async function deleteProject(projectId) {
        const confirmDelete = confirm('Are you sure you want to delete this project?');
        if (confirmDelete) {
            const response = await makeAuthenticatedRequest('http://localhost:8080/projects/delete', {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ projectId: projectId })
            });
            if (response) {
                alert('Project deleted successfully');
                loadProjects();
            } else {
                alert('Failed to delete project');
            }
        }
    }
        
    

    async function loadRequests() {
        mainContent.innerHTML = `
            <h2>Resource Requests</h2>
            <div id="unassignEmployee">
                <h3>Unassign Employee from Manager</h3>
                <input type="text" id="unassignEmployeeId" placeholder="Enter Employee ID">
                <button id="unassignEmployeeBtn">Unassign</button>
            </div>
            <table id="requestsTable">
                <thead>
                    <tr>
                        <th>Request ID</th>
                        <th>Manager ID</th>
                        <th>Employee ID</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        `;
    
        const requestsTableBody = document.querySelector('#requestsTable tbody');
        const unassignEmployeeBtn = document.getElementById('unassignEmployeeBtn');
        const unassignEmployeeId = document.getElementById('unassignEmployeeId');
    
        // Fetch and display all requests
        const requests = await makeAuthenticatedRequest('http://localhost:8080/requests/all');
        if (requests) {
            requestsTableBody.innerHTML = requests.map(request => `
                <tr>
                    <td>${request.requestId}</td>
                    <td>${request.managerId}</td>
                    <td>${request.employeeId}</td>
                    <td>${request.status}</td>
                    <td>
                        ${request.status === 'PENDING' ? `
                            <button class="accept-btn" data-request-id="${request.requestId}">Accept</button>
                            <button class="decline-btn" data-request-id="${request.requestId}">Decline</button>
                        ` : ''}
                    </td>
                </tr>
            `).join('');
        } else {
            requestsTableBody.innerHTML = '<tr><td colspan="5">No requests found</td></tr>';
        }
    
        // Event delegation for accept and decline buttons
        requestsTableBody.addEventListener('click', (event) => {
            if (event.target.classList.contains('accept-btn')) {
                const requestId = event.target.getAttribute('data-request-id');
                approveRequest(requestId, event.target);
            } else if (event.target.classList.contains('decline-btn')) {
                const requestId = event.target.getAttribute('data-request-id');
                declineRequest(requestId, event.target);
            }
        });
    
        // Unassign Employee button click event
        unassignEmployeeBtn.addEventListener('click', async () => {
            const employeeId = unassignEmployeeId.value.trim();
            if (employeeId) {
                const response = await makeAuthenticatedRequest(`http://localhost:8080/requests/unassign/${employeeId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                if (response) {
                    alert('Employee unassigned successfully');
                } else {
                    alert('check the employee');
                }
            } else {
                alert('Please enter an employee ID');
            }
        });
    }async function approveRequest(requestId, button) {
        const response = await makeAuthenticatedRequest(`http://localhost:8080/requests/approve/${requestId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        if (response) {
            alert('Request approved successfully');
            button.parentElement.innerHTML = 'APPROVED';
        } else {
            alert('check the employee ');
        }
    }
    
    async function declineRequest(requestId, button) {
        const response = await makeAuthenticatedRequest(`http://localhost:8080/requests/declineRequest/${requestId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });
        if (response) {
            alert('Request declined successfully');
            button.parentElement.innerHTML = 'DECLINED';
        } else {
            alert('check the employee');
        }
    }
    
    

    // Change password function
    function changePassword() {
        mainContent.innerHTML = `
            <h2>Change Password</h2>
            <form id="changePasswordForm" class="center-form">
                <label for="oldPassword">Old Password:</label>
                <input type="password" id="oldPassword" name="oldPassword" required>
                <label for="newPassword">New Password:</label>
                <input type="password" id="newPassword" name="newPassword" required>
                <label for="retypeNewPassword">Retype New Password:</label>
                <input type="password" id="retypeNewPassword" name="retypeNewPassword" required>
                <div class="button-group">
                    <button type="submit">Change Password</button>
                    <button type="button" id="forgotPasswordBtn">Forgot Password</button>
                    <button type="button" id="backBtn">Back</button>
                </div>
            </form>
        `;
    
        const changePasswordForm = document.getElementById('changePasswordForm');
        const forgotPasswordBtn = document.getElementById('forgotPasswordBtn');
        const backBtn = document.getElementById('backBtn');
    
        // Handle form submission for changing password
        changePasswordForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            
            const oldPassword = changePasswordForm.oldPassword.value;
            const newPassword = changePasswordForm.newPassword.value;
            const retypeNewPassword = changePasswordForm.retypeNewPassword.value;
    
            if (newPassword !== retypeNewPassword) {
                alert('New passwords do not match.');
                return;
            }
    
            try {
                const response = await makeAuthenticatedRequest('http://localhost:8080/user/change-password', {
                    method: 'POST',
                    body: JSON.stringify({ oldPassword, newPassword })
                });
    
                if (response) {
                    alert('Password changed successfully.');
                    loadAllUsers(); // Or navigate back to the admin dashboard
                } else {
                    console.error('Failed to change password');
                }
            } catch (error) {
                console.error('Error during password change:', error);
                alert('An error occurred. Please try again.');
            }
        });
    
        // Handle forgot password button click
        forgotPasswordBtn.addEventListener('click', async () => {
            const email = prompt('Please enter your email:');
            if (!email) return;
    
            try {
                const response = await makeAuthenticatedRequest('http://localhost:8080/user/forgot-password', {
                    method: 'POST',
                    body: JSON.stringify({ email })
                });
    
                if (response) {
                    alert('Check your email for password reset instructions.');
                } else {
                    console.error('Failed to request password reset');
                }
            } catch (error) {
                console.error('Error during forgot password request:', error);
                alert('An error occurred. Please try again.');
            }
        });
    
        // Handle back button click
        backBtn.addEventListener('click', loadAllUsers); // Or navigate back to the appropriate section
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
