import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContractType, ContractType } from 'app/shared/model/contract-type.model';
import { ContractTypeService } from './contract-type.service';
import { ContractTypeComponent } from './contract-type.component';
import { ContractTypeDetailComponent } from './contract-type-detail.component';
import { ContractTypeUpdateComponent } from './contract-type-update.component';

@Injectable({ providedIn: 'root' })
export class ContractTypeResolve implements Resolve<IContractType> {
  constructor(private service: ContractTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContractType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contractType: HttpResponse<ContractType>) => {
          if (contractType.body) {
            return of(contractType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContractType());
  }
}

export const contractTypeRoute: Routes = [
  {
    path: '',
    component: ContractTypeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContractTypeDetailComponent,
    resolve: {
      contractType: ContractTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContractTypeUpdateComponent,
    resolve: {
      contractType: ContractTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContractTypeUpdateComponent,
    resolve: {
      contractType: ContractTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];
