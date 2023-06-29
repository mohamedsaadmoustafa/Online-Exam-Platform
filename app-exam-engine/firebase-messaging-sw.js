importScripts('https://www.gstatic.com/firebasejs/8.7.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.7.0/firebase-messaging.js');

firebase.initializeApp({
  apiKey: "AIzaSyCjwGYK55ER3TdgjehITOvkQFykBC_hdqc",
  authDomain: "exam-engine-microservice.firebaseapp.com",
  projectId: "exam-engine-microservice",
  storageBucket: "exam-engine-microservice.appspot.com",
  messagingSenderId: "148184916689",
  appId: "1:148184916689:web:f6ccbc67e2f0e0c6761bf0"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: '/assets/icons/icon-512x512.png',
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});
