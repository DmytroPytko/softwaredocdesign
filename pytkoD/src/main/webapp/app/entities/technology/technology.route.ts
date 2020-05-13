import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITechnology, Technology } from 'app/shared/model/technology.model';
import { TechnologyService } from './technology.service';
import { TechnologyComponent } from './technology.component';
import { TechnologyDetailComponent } from './technology-detail.component';
import { TechnologyUpdateComponent } from './technology-update.component';

@Injectable({ providedIn: 'root' })
export class TechnologyResolve implements Resolve<ITechnology> {
  constructor(private service: TechnologyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITechnology> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((technology: HttpResponse<Technology>) => {
          if (technology.body) {
            return of(technology.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Technology());
  }
}

export const technologyRoute: Routes = [
  {
    path: '',
    component: TechnologyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TechnologyDetailComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  }
];
