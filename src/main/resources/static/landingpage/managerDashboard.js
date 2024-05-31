document.addEventListener('DOMContentLoaded', () => {
    const viewEmployeesBtn = document.getElementById('viewEmployeesBtn');
    const viewManagersBtn = document.getElementById('viewManagersBtn');
    const viewProjectsBtn = document.getElementById('viewProjectsBtn');
    const filterEmployeesBtn = document.getElementById('filterEmployeesBtn');
    const requestEmployeesBtn = document.getElementById('requestEmployeesBtn');
    const changePasswordBtn = document.getElementById('changePasswordBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const mainContent = document.getElementById('mainContent');

    // Event listeners for buttons
    viewEmployeesBtn.addEventListener('click', () => loadViewEmployees());
    viewManagersBtn.addEventListener('click', () => loadViewManagers());
    viewProjectsBtn.addEventListener('click', () => loadViewProjects());
    filterEmployeesBtn.addEventListener('click', () => loadFilterEmployees());
    requestEmployeesBtn.addEventListener('click', () => loadRequestEmployees());
    changePasswordBtn.addEventListener('click', () => changePassword());
    logoutBtn.addEventListener('click', () => logout());

    // Load view employees function
    async function loadViewEmployees() {
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
                    <td>${Array.isArray(employee.skills) ? employee.skills.join(', ') : ''}</td>
                </tr>
            `).join('');
        } else {
            employeesTableBody.innerHTML = '<tr><td colspan="10">No employees found</td></tr>';
        }

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
                        <p>Skills: ${Array.isArray(employee.skills) ? employee.skills.join(', ') : ''}</p>
                    `;
                } else {
                    employeeDetails.innerHTML = '<p>No employee found with the given ID</p>';
                }
            } else {
                alert('Please enter an employee ID');
            }
        });
    }


    // Load view managers function
    async function loadViewManagers() {
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

        const managers = await makeAuthenticatedRequest('http://localhost:8080/manager/getAll');
        if (managers) {
            managersTableBody.innerHTML = managers.map(manager => `
                <tr>
                    <td>${manager.mgrId}</td>
                    <td>${manager.name}</td>
                    <td>${manager.contactNumber}</td>
                    <td>${manager.email}</td>
                    <td>${manager.designation}</td>
                    <td>${Array.isArray(manager.employees) ? manager.employees.join(', ') : ''}</td>
                    <td>${Array.isArray(manager.projectNames) ? manager.projectNames.join(', ') : ''}</td>
                </tr>
            `).join('');
        } else {
            managersTableBody.innerHTML = '<tr><td colspan="7">No managers found</td></tr>';
        }

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
                        <p>Employees: ${Array.isArray(manager.employees) ? manager.employees.join(', ') : ''}</p>
                        <p>Project Names: ${Array.isArray(manager.projectNames) ? manager.projectNames.join(', ') : ''}</p>
                    `;
                } else {
                    managerDetails.innerHTML = '<p>No manager found with the given ID</p>';
                }
            } else {
                alert('Please enter a manager ID');
            }
        });
    }


    // Load view projects function
    async function loadViewProjects() {
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

    async function loadFilterEmployees() {
        mainContent.innerHTML = `
            <h2>Filter Employees</h2>
            <input type="text" id="filterSkill" placeholder="Enter Skill">
            <button id="searchBySkillBtn">Search by Skill</button>
            <button id="viewUnassignedBtn">View Unassigned Employees</button>
            <div id="filterResults"></div>
            <table id="filterEmployeesTable">
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

        const filterSkill = document.getElementById('filterSkill');
        const searchBySkillBtn = document.getElementById('searchBySkillBtn');
        const viewUnassignedBtn = document.getElementById('viewUnassignedBtn');
        const filterResults = document.getElementById('filterResults');
        const filterEmployeesTableBody = document.querySelector('#filterEmployeesTable tbody');

        searchBySkillBtn.addEventListener('click', async () => {
            const skill = filterSkill.value.trim();
            if (skill) {
                const employees = await makeAuthenticatedRequest('http://localhost:8080/employee/employeesBySkill', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ skill })
                });
                if (employees) {
                    filterEmployeesTableBody.innerHTML = employees.map(employee => `
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
                            <td>${Array.isArray(employee.skills) ? employee.skills.join(', ') : ''}</td>
                        </tr>
                    `).join('');
                } else {
                    filterEmployeesTableBody.innerHTML = '<tr><td colspan="10">No employees found</td></tr>';
                }
            } else {
                alert('Please enter a skill');
            }
        });

        viewUnassignedBtn.addEventListener('click', async () => {
            const employees = await makeAuthenticatedRequest('http://localhost:8080/employee/assignable');
            if (employees) {
                filterEmployeesTableBody.innerHTML = employees.map(employee => `
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
                        <td>${Array.isArray(employee.skills) ? employee.skills.join(', ') : ''}</td>
                    </tr>
                `).join('');
            } else {
                filterEmployeesTableBody.innerHTML = '<tr><td colspan="10">No unassigned employees found</td></tr>';
            }
        });
    }

    async function loadRequestEmployees() {
        mainContent.innerHTML = `
            <h2>Request Employees</h2>
            <div>
                <label for="managerId">Manager ID:</label>
                <input type="text" id="managerId" placeholder="Enter Manager ID">
                <label for="employeeId">Employee ID:</label>
                <input type="text" id="employeeId" placeholder="Enter Employee ID">
                <button id="sendRequestBtn">Send Request</button>
            </div>
            <h3>All Requests</h3>
            <div id="requestResults"></div>
            <table id="requestEmployeesTable">
                <thead>
                    <tr>
                        <th>Manager ID</th>
                        <th>Employee ID</th>
                        <th>Status</th>
                        <th>Request Date</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        `;

        const managerIdInput = document.getElementById('managerId');
        const employeeIdInput = document.getElementById('employeeId');
        const sendRequestBtn = document.getElementById('sendRequestBtn');
        const requestEmployeesTableBody = document.querySelector('#requestEmployeesTable tbody');

        sendRequestBtn.addEventListener('click', async () => {
            const managerId = managerIdInput.value.trim();
            const employeeId = employeeIdInput.value.trim();

            if (managerId && employeeId) {
                const response = await makeAuthenticatedRequest('http://localhost:8080/requests/send', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ managerId, employeeId })
                });

                if (response) {
                    alert('Request sent successfully');
                    loadAllRequests(); // Refresh the list of requests
                } else {
                    alert('Failed to send request');
                }
            } else {
                alert('Please enter both Manager ID and Employee ID');
            }
        });

        async function loadAllRequests() {
            const requests = await makeAuthenticatedRequest('http://localhost:8080/requests/all');
            if (requests) {
                requestEmployeesTableBody.innerHTML = requests.map(request => `
                    <tr>
                        <td>${request.managerId}</td>
                        <td>${request.employeeId}</td>
                        <td>${request.status}</td>
                        <td>${request.requestDate}</td>
                    </tr>
                `).join('');
            } else {
                requestEmployeesTableBody.innerHTML = '<tr><td colspan="4">No requests found</td></tr>';
            }
        }

        // Initial load of all requests
        loadAllRequests();
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

    // Initially load view employees
    loadViewEmployees();
});
