document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signup-form');
    const signupModal = document.getElementById('signup-modal');
    const signupModalCloseBtn = signupModal.querySelector('.close-btn');
    const redirectToTaskList = () => {
        console.log('Redirecting to /task/list');
        window.location.href = '/task/list';
    };

    document.getElementById('open-signup-modal-btn').addEventListener('click', () => {
        signupModal.style.display = 'block';
    });
    signupModalCloseBtn.addEventListener('click', () => {
        signupModal.style.display = 'none';
    });
    window.addEventListener('click', (event) => {
        if (event.target == signupModal) {
            signupModal.style.display = 'none';
        }
    });

    let userData = {
        username: '',
        userId: '',
        todos: []
    };

    signupForm.onsubmit = (e) => {
        e.preventDefault();
        const formData = new FormData(signupForm);
        const name = formData.get('name');
        const userEmail = formData.get('userEmail');
        const username = formData.get('username');
        const password = formData.get('password');

        fetch('/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ name, userEmail, username, password })

        }).then(response => {
            console.log('Response received', response);
           if (response.ok) {
                      return response.json();
          }
            throw new Error('Network response was not ok.');
        }).then(data => {
            if (!data) {
                    throw new Error('Response data is undefined');
                }
            console.log('Data received', data);
            userData.username = username;
            userData.userId = data.userId;
            localStorage.setItem('userData', JSON.stringify(userData));
            if (data.redirectUrl) {
                        window.location.href = data.redirectUrl; // Redirect to the task list page
                    }
            }).catch(error => {
            console.error('Error:', error);
            alert('Error: ' + error.message);
        });
    };
});
