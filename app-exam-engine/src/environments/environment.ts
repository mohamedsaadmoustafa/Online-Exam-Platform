export const environment = {
  production: false, // true
  examEngineApiUrl: 'http://localhost:8081/exam/',
  questionBankApiUrl: 'http://localhost:8083/question/',
  authenticationApiUrl: 'http://localhost:8009/api/',
  ///////////////////////////
  /// edit this
  //////////////////////////
  notificationsApiUrl: 'http://localhost:8005/',

  levels: ["Easy", "Medium", "Hard"],
  categories: ["Design", "Maths", "Literature", "Languages", "Geography", "Philosophy", "History", "Art", "Biology", "Astronomy", "Nature"],
  subcategories: {
    'Design': ['Graphic design', 'Industrial design', 'Fashion design', 'Interior design'],
    'Maths': ['Algebra', 'Calculus', 'Geometry', 'Statistics', 'Probability theory'],
    'Literature': ['Poetry', 'Fiction', 'Non-fiction', 'Drama', 'Literary criticism'],
    'Languages': ['Spanish', 'French', 'German', 'Chinese', 'Arabic', 'Sign language', 'Programming languages'],
    'Geography': ['Physical geography', 'Human geography', 'Cartography', 'Environmental geography'],
    'Philosophy': ['Ethics', 'Metaphysics', 'Epistemology', 'Political philosophy'],
    'History': ['Ancient history', 'Medieval history', 'Modern history', 'World history', 'Cultural history'],
    'Art': ['Painting', 'Sculpture', 'Photography', 'Performance art', 'Digital art'],
    'Biology': ['Genetics', 'Ecology', 'Microbiology', 'Anatomy', 'Physiology'],
    'Astronomy': ['Astrophysics', 'Cosmology', 'Planetary science', 'Observational astronomy'],
    'Nature': ['Botany', 'Zoology', 'Conservation', 'Geology', 'Climatology']
  }
};
