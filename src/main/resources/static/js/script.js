document.addEventListener('DOMContentLoaded', () => {
    const openCreateTodoModalBtn = document.getElementById('open-create-todo-modal-btn');
    const openLoginModalBtn = document.getElementById('open-login-modal-btn');
    const openSignupModalBtn = document.getElementById('open-signup-modal-btn');
    const todoListContainer = document.getElementById('default-task-list');
    const todoList = document.getElementById('todo-list');
    const newTaskForm = document.getElementById('new-task-form');
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const addTaskModal = document.getElementById('add-task-modal');
    const loginModal = document.getElementById('login-modal');
    const signupModal = document.getElementById('signup-modal');
    const clearSelectedButton = document.querySelector('button');

     if (todoList) {
             const handleTaskClick = (event) => {
                 const target = event.target;
                 if (target.tagName === 'INPUT') {
                     const taskId = target.closest('.task-item').dataset.id;
                     deleteTask(taskId);
                 }
             };

     const deleteTask = (taskId) => {
                 fetch(`/tasks/remove?id=${taskId}`, {
                     method: 'DELETE'
                 })
                 .then(response => {
                     if (response.ok) {
                         const taskItem = document.querySelector(`.task-item[data-id="${taskId}"]`);
                         if (taskItem) {
                             taskItem.remove();
                         }
                     } else {
                         console.error('Error deleting task:', response.status);
                     }
                 })
                 .catch(error => {
                     console.error('Error deleting task:', error);
                 });
             };

             todoList.addEventListener('click', handleTaskClick);
         } else {
             console.warn('Element with id "todo-list" not found.');
         }

    if (clearSelectedButton) {
        clearSelectedButton.addEventListener('click', () => {
            const selectedTasks = document.querySelectorAll('.task-item input:checked');
            selectedTasks.forEach(task => {
                const taskId = task.closest('.task-item').dataset.id;
                deleteTask(taskId);
            });
        });
    } else {
        console.warn('Clear selected button not found.');
    }

    let userData = {
        username: '',
        userId: '',
        todos: []
    };

    const redirectToTaskList = () => {
        window.location.href = '/list-test';
    };

    const replaceTodoList = () => {
        if (todoList) {
            todoList.innerHTML = '';
            userData.todos.forEach(task => {
                const li = document.createElement('li');
                li.className = 'task-item';
                li.innerHTML = `
                    <input type="checkbox" ${task.status === 'COMPLETED' ? 'checked' : ''} />
                    <a href="/detail/${task.id}">${task.title}</a>
                `;
                todoList.appendChild(li);
            });
        } else {
            console.warn('Element with id "todo-list" not found.');
        }
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
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok.');
        }).then(data => {
            userData.username = username;
            userData.userId = data.userId;
            localStorage.setItem('userData', JSON.stringify(userData));
            redirectToTaskList();
        }).catch(error => {
            console.error('Error:', error);
            alert('Error: ' + error.message);
        });
    };

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
    if(newTaskForm){
    newTaskForm.onsubmit = (e) => {
        e.preventDefault();
        if (!isLoggedIn()) {
            alert("Please log in to create a task.");
            return;
        }
        if (!userData.userId) {
            alert("User ID is not set. Please log in again.");
            return;
        }
        const title = newTaskForm.title.value;
        const description = newTaskForm.description.value;
        const priority = newTaskForm.priority.value;
        const deadline = newTaskForm.deadline.value;
        const status = newTaskForm.status.value;

        const formattedDeadline = new Date(deadline).toISOString().split('T')[0];

        const formData = new URLSearchParams({
            title,
            description,
            priority,
            deadline: formattedDeadline,
            status,
            userId: userData.userId
        });

        fetch('/tasks/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString(),
        }).then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        }).then(data => {
            userData.todos.push(data);
            replaceTodoList();
            newTaskForm.reset();
            closeModal(addTaskModal);
        }).catch(error => {
            console.error('Error:', error);
            alert('Error: ' + error.message);
        });
    };
}


    if (openCreateTodoModalBtn) {
        openCreateTodoModalBtn.onclick = () => {
            if (!isLoggedIn()) {
                alert("Please log in to create a task.");
                return;
            }
            openModal(addTaskModal);
        };
    }
    if (openLoginModalBtn) {
        openLoginModalBtn.onclick = () => openModal(loginModal);
    }

    if (openSignupModalBtn) {
        openSignupModalBtn.onclick = () => openModal(signupModal);
    }


    const handleTaskTitleClick = (event) => {
        const target = event.target;
        if (target.tagName === 'A') {
            event.preventDefault();
            const href = target.getAttribute('href');
            const taskIdIndex = href.lastIndexOf('/');
            if (taskIdIndex !== -1) {
                const taskId = href.substring(taskIdIndex + 1);
                window.location.href = `/detail/${taskId}`;
            } else {
                console.error('Invalid URL format:', href);
            }
        }
    };

    if (todoListContainer) {
        todoListContainer.addEventListener('click', handleTaskTitleClick);
    } else {
        console.warn('Element with id "default-task-list" not found.');
    }

});