const openLoginModalBtn = document.getElementById('open-login-modal-btn');
const loginModal = document.getElementById('login-modal');
const loginForm = document.getElementById('login-form');

const redirectToTaskList = () => {
    window.location.href = '/list-test';
};
 if (openLoginModalBtn) {
        openLoginModalBtn.onclick = () => openModal(loginModal);
    }

loginForm.onsubmit = (e) => {
        e.preventDefault();
        const formData = new FormData(loginForm);
        const username = formData.get('username');
        const password = formData.get('password');

        fetch('/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ username, password })

        } ).then(response => {
            if (response.ok) {
                return response.json();
                alert("hello 1");
            }
            throw new Error('Network response was not ok.');
        }).then(data => {
            alert("hello 2");
            userData.username = username;
            userData.userId = data.userId;
            localStorage.setItem('userData', JSON.stringify(userData));
            redirectToTaskList();
        }).catch(error => {
            console.error('Error:', error);
        });
    };
     const closeModal = (modal) => {
            modal.style.display = 'none';
        };

        const openModal = (modal) => {
            modal.style.display = 'block';
        };

        document.querySelectorAll('.close-btn').forEach(btn => {
            btn.onclick = () => closeModal(btn.closest('.modal'));
        });