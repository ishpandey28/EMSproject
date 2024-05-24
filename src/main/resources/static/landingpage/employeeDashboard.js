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
        mainContent.innerHTML = '<h2>All Employees</h2>';
        const employees = await makeAuthenticatedRequest('http://localhost:8080/user/employees');
        const employeesTable = document.createElement('table');
        employeesTable.innerHTML = `
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Contact Number</th>
                    <th>Status</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                ${employees.map(emp => `
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.name}</td>
                        <td>${emp.email}</td>
                        <td>${emp.contactNumber}</td>
                        <td>${emp.status}</td>
                        <td>${emp.role}</td>
                    </tr>`).join('')}
            </tbody>
        `;
        mainContent.appendChild(employeesTable);
    }

    async function loadManagers() {
        mainContent.innerHTML = '<h2>All Managers</h2>';
        const managers = await makeAuthenticatedRequest('http://localhost:8080/user/managers');
        const managersTable = document.createElement('table');
        managersTable.innerHTML = `
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Contact Number</th>
                    <th>Status</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                ${managers.map(manager => `
                    <tr>
                        <td>${manager.id}</td>
                        <td>${manager.name}</td>
                        <td>${manager.email}</td>
                        <td>${manager.contactNumber}</td>
                        <td>${manager.status}</td>
                        <td>${manager.role}</td>
                    </tr>`).join('')}
            </tbody>
        `;
        mainContent.appendChild(managersTable);
    }

    async function loadSkills() {
        mainContent.innerHTML = '<h2>Skills</h2>';
        const skills = await makeAuthenticatedRequest('http://localhost:8080/employee/skills');
        const skillsTable = document.createElement('table');
        skillsTable.innerHTML = `
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Skill Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                ${skills.map(skill => `
                    <tr>
                        <td>${skill.id}</td>
                        <td>${skill.name}</td>
                        <td>
                            <button class="update-skill-btn" data-skill-id="${skill.id}">Update</button>
                            <button class="delete-skill-btn" data-skill-id="${skill.id}">Delete</button>
                        </td>
                    </tr>`).join('')}
            </tbody>
        `;
        mainContent.appendChild(skillsTable);

        // Add skill form
        const addSkillForm = document.createElement('form');
        addSkillForm.innerHTML = `
            <h3>Add New Skill</h3>
            <input type="text" id="newSkillName" placeholder="Skill Name">
            <button type="submit">Add Skill</button>
        `;
        mainContent.appendChild(addSkillForm);

        addSkillForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const skillName = document.getElementById('newSkillName').value;
            await makeAuthenticatedRequest('http://localhost:8080/employee/skills', {
                method: 'POST',
                body: JSON.stringify({ name: skillName })
            });
            loadSkills(); // Refresh the list
        });

        // Event listeners for update and delete buttons
        document.querySelectorAll('.update-skill-btn').forEach(button => {
            button.addEventListener('click', async (event) => {
                const skillId = event.target.dataset.skillId;
                const newSkillName = prompt('Enter new skill name:');
                if (newSkillName) {
                    await makeAuthenticatedRequest(`http://localhost:8080/employee/skills/${skillId}`, {
                        method: 'PUT',
                        body: JSON.stringify({ name: newSkillName })
                    });
                    loadSkills(); // Refresh the list
                }
            });
        });

        document.querySelectorAll('.delete-skill-btn').forEach(button => {
            button.addEventListener('click', async (event) => {
                const skillId = event.target.dataset.skillId;
                await makeAuthenticatedRequest(`http://localhost:8080/employee/skills/${skillId}`, {
                    method: 'DELETE'
                });
                loadSkills(); // Refresh the list
            });
        });
    }

    async function loadProfile() {
        mainContent.innerHTML = '<h2>Your Profile</h2>';
        const profile = await makeAuthenticatedRequest('http://localhost:8080/employee/profile');
        const profileDiv = document.createElement('div');
        profileDiv.innerHTML = `
            <p>Emp ID: ${profile.empId}</p>
            <p>User ID: ${profile.userId}</p>
            <p>Name: ${profile.name}</p>
            <p>Contact Number: ${profile.contactNumber}</p>
            <p>Email: ${profile.email}</p>
            <p>Manager: ${profile.manager}</p>
            <p>Project: ${profile.project}</p>
            <p>Skills: ${profile.skills.join(', ')}</p>
            <p>Designation: ${profile.designation}</p>
            <p>Project Status: ${profile.projectStatus}</p>
        `;
        mainContent.appendChild(profileDiv);
    }

    function changePassword() {
        mainContent.innerHTML = '<h2>Change Password</h2>';
        // Implement change password functionality here
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

    // Initially load all employees
    loadEmployees();
});
