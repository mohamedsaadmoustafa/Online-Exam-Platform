import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {StorageService} from "./storage.service";

export const AuthGuard: CanActivateFn = (route, state) => {
  const storageService: StorageService = inject(StorageService);
  const router: Router = inject(Router);
  if (storageService.isLoggedIn()) {
    const userRole = storageService.getUserRole();
    if (route.data['role'].includes(userRole)) return true;
  }
  router.navigate(['/login']);
  return false;
}
