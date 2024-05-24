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
        mainContent.innerHTML = '<h2>All Employees</h2>';
        const employees = await makeAuthenticatedRequest('http://localhost:8080/manager/viewEmployees');
        // Add logic to display employees in a table
    }

    // Load view managers function
    async function loadViewManagers() {
        mainContent.innerHTML = '<h2>All Managers</h2>';
        const managers = await makeAuthenticatedRequest('http://localhost:8080/manager/viewManagers');
        // Add logic to display managers in a table
    }

    // Load view projects function
    async function loadViewProjects() {
        mainContent.innerHTML = '<h2>All Projects</h2>';
        const projects = await makeAuthenticatedRequest('http://localhost:8080/manager/viewProjects');
        // Add logic to display projects in a table
    }

    // Load filter employees function
    async function loadFilterEmployees() {
        mainContent.innerHTML = `
            <h2>Filter Employees</h2>
            <form id="filterForm">
                <label for="skill">Skill:</label>
                <input type="text" id="skill" name="skill">
                <label for="unassigned">Unassigned:</label>
                <input type="checkbox" id="unassigned" name="unassigned">
                <button type="submit">Filter</button>
            </form>
            <div id="filteredEmployees"></div>
        `;

        document.getElementById('filterForm').addEventListener('submit', async (event) => {
            event.preventDefault();
            const skill = document.getElementById('skill').value;
            const unassigned = document.getElementById('unassigned').checked;
            const filteredEmployees = await makeAuthenticatedRequest('http://localhost:8080/manager/filterEmployees', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ skill, unassigned })
            });
            // Add logic to display filtered employees in #filteredEmployees
        });
    }

    // Load request employees function
    async function loadRequestEmployees() {
        mainContent.innerHTML = `
            <h2>Request Employees</h2>
            <form id="requestForm">
                <label for="project">Project:</label>
                <input type="text" id="project" name="project">
                <label for="skills">Skills:</label>
                <input type="text" id="skills" name="skills">
                <button type="submit">Request</button>
            </form>
        `;

        document.getElementById('requestForm').addEventListener('submit', async (event) => {
            event.preventDefault();
            const project = document.getElementById('project').value;
            const skills = document.getElementById('skills').value;
            const response = await makeAuthenticatedRequest('http://localhost:8080/manager/requestEmployees', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ project, skills })
            });
            // Add logic to handle the response
        });
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
