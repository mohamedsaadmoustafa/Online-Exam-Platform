import {CanActivateFn, Router} from '@angular/router';
import {StorageService} from "./storage.service";
import {inject} from "@angular/core";

export const AuthGuard: CanActivateFn = (route, state) => {
  const storageService: StorageService = inject(StorageService);
  const router: Router = inject(Router);
  if (storageService.isLoggedIn()) {
    const userRole = storageService.getUserRole();
    if (route.data['role'].includes(userRole)) return true;
  }
  router.navigate(['/dashboard']);
  return false;
}

