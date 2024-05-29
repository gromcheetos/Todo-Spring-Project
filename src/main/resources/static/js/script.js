function showMoreItems() {
  document.querySelectorAll('.hidden-task').forEach(function(item) {
    item.style.display = 'flex';
  });
  document.getElementById('more-link').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', () => {
    const openCreateTodoModalBtn = document.getElementById('open-create-todo-modal-btn');
    const openLoginModalBtn = document.getElementById('open-login-modal-btn');
    const openSignupModalBtn = document.getElementById('open-signup-modal-btn');
    const logoutBtn = document.getElementById('logout-btn');
    const todoListContainer = document.getElementById('default-task-list');
    const todoList = document.getElementById('todo-list');
    const newTaskForm = document.getElementById('new-task-form');
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const addTaskModal = document.getElementById('add-task-modal');
    const loginModal = document.getElementById('login-modal');
    const signupModal = document.getElementById('signup-modal');
    const showMoreItemsLink = document.getElementById('show-more-items');
    const clearSelectedButton = document.querySelector('button');

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

          // Event listener for "Clear Selected" button
          clearSelectedButton.addEventListener('click', () => {
              const selectedTasks = document.querySelectorAll('.task-item input:checked');
              selectedTasks.forEach(task => {
                  const taskId = task.closest('.task-item').dataset.id;
                  deleteTask(taskId);
              });
          });

    let userData = {
        username: '',
        userId: '',
        todos: []
    };

    const isLoggedIn = () => {
        const retrievedUserData = JSON.parse(localStorage.getItem('userData'));
        return retrievedUserData.username !== '';
    };

    const replaceTodoList = () => {
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
    };

    const toggleAuthButtons = () => {
        if (isLoggedIn()) {
            openLoginModalBtn.style.display = 'none';
            openSignupModalBtn.style.display = 'none';
            logoutBtn.style.display = 'block';
        } else {
            openLoginModalBtn.style.display = 'block';
            openSignupModalBtn.style.display = 'block';
            logoutBtn.style.display = 'none';
        }
    };

    const logout = () => {
        userData = {
            username: '',
            userId: '',
            todos: []
        };
        replaceTodoList();
        toggleAuthButtons();
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
            replaceTodoList();
            closeModal(signupModal);
            toggleAuthButtons();
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
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Network response was not ok.');
        }).then(data => {
            userData.username = username;
            userData.userId = data.userId;
            localStorage.setItem('userData', JSON.stringify(userData));
            replaceTodoList();
            closeModal(loginModal);
            toggleAuthButtons();
        }).catch(error => {
            console.error('Error:', error);
        });
    };

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

    const fetchTasksByStatus = (status) => {
        fetch(`/tasks/filter/status?status=${status}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text) });
                }
                return response.json();
            })
            .then(tasks => {
                userData.todos = tasks;
                replaceTodoList();
            })
            .catch(error => {
                console.error('Error fetching tasks by status:', error);
                alert('Error: ' + error.message);
            });
    };

    document.getElementById('filter-todo').addEventListener('click', () => {
        fetchTasksByStatus('TO_DO');
    });
    document.getElementById('filter-in-progress').addEventListener('click', () => {
        fetchTasksByStatus('IN_PROGRESS');
    });
    document.getElementById('filter-done').addEventListener('click', () => {
        fetchTasksByStatus('DONE');
    });

    openCreateTodoModalBtn.onclick = () => {
        if (!isLoggedIn()) {
            alert("Please log in to create a task.");
            return;
        }
        openModal(addTaskModal);
    };
    openLoginModalBtn.onclick = () => openModal(loginModal);
    openSignupModalBtn.onclick = () => openModal(signupModal);

    showMoreItemsLink.onclick = showMoreItems;
    logoutBtn.onclick = logout;
    toggleAuthButtons();

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


    todoListContainer.addEventListener('click', handleTaskTitleClick);

});
