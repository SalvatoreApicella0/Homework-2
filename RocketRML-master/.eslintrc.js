module.exports = {
  extends: ['airbnb-base', 'prettier'],
  rules: {
    'prefer-promise-reject-errors': 'off',
    'no-use-before-define': 'off',
    'no-param-reassign': 'off',
    'max-len': 'off',
    'guard-for-in': 'off',
    'no-restricted-syntax': 'off',
    'no-throw-literal': 'off',
    'no-case-declarations': 'off',
    'prefer-destructuring': 'off',
    'new-cap': 'off',
    'no-plusplus': 'off',
    'no-useless-concat': 'off',
    'no-return-assign': 'off',
    'linebreak-style': 'off',
    'no-await-in-loop': 'warn',
    'no-underscore-dangle': 'off',
  },
  env: {
    mocha: true,
  },
};
