export const oktaConfig = {
    clientId: '0oawfw3tmxFpfa39H697',
    issuer: 'https://integrator-5967613.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
}
