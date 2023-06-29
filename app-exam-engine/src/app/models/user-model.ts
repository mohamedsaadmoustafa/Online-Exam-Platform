export interface UserModel{
  self: null;
  id: string;
  origin: null;
  createdTimestamp: number;
  username: string;
  enabled: boolean;
  totp: boolean;
  emailVerified: boolean;
  firstName: string;
  lastName: string;
  email: string;
  federationLink: null;
  serviceAccountClientId: null;
  attributes: null;
  credentials: null;
  disableableCredentialTypes: any[];
  requiredActions: any[];
  federatedIdentities: null;
  realmRoles: null;
  clientRoles: null;
  clientConsents: null;
  notBefore: number;
  applicationRoles: null;
  socialLinks: null;
  groups: null;
  access: {
    manageGroupMembership: boolean;
    view: boolean;
    mapRoles: boolean;
    impersonate: boolean;
    manage: boolean;
  };
}
