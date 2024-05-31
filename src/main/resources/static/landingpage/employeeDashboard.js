document.addEventListener('DOMContentLoaded', () => {
    const viewEmployeesBtn = document.getElementById('viewEmployeesBtn');
    const viewManagersBtn = document.getElementById('viewManagersBtn');
    const skillsBtn = document.getElementById('skillsBtn');
    const yourProfileBtn = document.getElementById('yourProfileBtn');
    const changePasswordBtn = document.getElementById('changePasswordBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const mainContent = document.getElementById('mainContent');

    viewEmployeesBtn.addEventListener('click', () => loadEmployees());
    viewManagersBtn.addEventListener('click', () => loadManagers());
    skillsBtn.addEventListener('click', () => loadSkills());
    yourProfileBtn.addEventListener('click', () => loadProfile());
    changePasswordBtn.addEventListener('click', () => changePassword());
    logoutBtn.addEventListener('click', () => logout());

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

    async function loadSkills() {
        mainContent.innerHTML = `
            <h2>Skills</h2>
            <input type="text" id="skillEmployeeId" placeholder="Enter Employee ID">
            <input type="text" id="skillNames" placeholder="Enter Skills (comma separated)">
            <button id="addSkillsBtn">Add Skills</button>
            <button id="updateSkillsBtn">Update Skills</button>
            <button id="deleteSkillsBtn">Delete Skills</button>
            <div id="skillsMessage"></div>
        `;

        const skillEmployeeId = document.getElementById('skillEmployeeId');
        const skillNames = document.getElementById('skillNames');
        const addSkillsBtn = document.getElementById('addSkillsBtn');
        const updateSkillsBtn = document.getElementById('updateSkillsBtn');
        const deleteSkillsBtn = document.getElementById('deleteSkillsBtn');
        const skillsMessage = document.getElementById('skillsMessage');

        addSkillsBtn.addEventListener('click', () => manageSkills('add'));
        updateSkillsBtn.addEventListener('click', () => manageSkills('update'));
        deleteSkillsBtn.addEventListener('click', () => manageSkills('delete'));

        async function manageSkills(action) {
            const employeeId = parseInt(skillEmployeeId.value.trim());
            const skillsArray = skillNames.value.trim().split(',').map(skill => skill.trim());

            if (!employeeId || !skillsArray.length) {
                alert('Please enter both Employee ID and Skills');
                return;
            }

            const urlMap = {
                add: 'http://localhost:8080/skills/add',
                update: 'http://localhost:8080/skills/update',
                delete: 'http://localhost:8080/skills/delete'
            };

            const payload = {
                employeeId: employeeId,
                skillNames: skillsArray
            };

            const response = await makeAuthenticatedRequest(urlMap[action], {
                method: action === 'add' ? 'POST' : action === 'update' ? 'PUT' : 'DELETE',
                body: JSON.stringify(payload)
            });

            if (response) {
                skillsMessage.innerHTML = `${action.charAt(0).toUpperCase() + action.slice(1)} skills successfully!`;
            } else {
                skillsMessage.innerHTML = `Failed to ${action} skills.`;
            }
        }
    }

    async function loadProfile() {
        const user = await makeAuthenticatedRequest('http://localhost:8080/employee/current');
        if (user) {
            mainContent.innerHTML = `
                <h2>Your Profile</h2>
                <div id="profileDetails">
                    <table>
                        <tr><th>ID</th><td>${user.empId}</td></tr>
                        <tr><th>Name</th><td>${user.name}</td></tr>
                        <tr><th>Contact Number</th><td>${user.contactNumber}</td></tr>
                        <tr><th>Email</th><td>${user.email}</td></tr>
                        <tr><th>Designation</th><td>${user.designation}</td></tr>
                        <tr><th>Project Status</th><td>${user.projectStatus}</td></tr>
                        <tr><th>Assigned</th><td>${user.assigned}</td></tr>
                        <tr><th>Manager Name</th><td>${user.managerName}</td></tr>
                        <tr><th>Project Name</th><td>${user.projectName}</td></tr>
                        <tr><th>Skills</th><td>${Array.isArray(user.skills) ? user.skills.join(', ') : ''}</td></tr>
                    </table>
                </div>
            `;
        } else {
            mainContent.innerHTML = '<p>Unable to load profile details.</p>';
        }
    }

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

    function logout() {
        localStorage.removeItem('jwtToken');
        window.location.href = 'landingpage.html';
    }

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

    loadEmployees();
});
