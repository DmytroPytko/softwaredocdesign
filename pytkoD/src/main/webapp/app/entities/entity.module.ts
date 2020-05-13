import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.MicrosoftEmployeeModule)
      },
      {
        path: 'resource',
        loadChildren: () => import('./resource/resource.module').then(m => m.MicrosoftResourceModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.MicrosoftClientModule)
      },
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.MicrosoftProjectModule)
      },
      {
        path: 'contract-type',
        loadChildren: () => import('./contract-type/contract-type.module').then(m => m.MicrosoftContractTypeModule)
      },
      {
        path: 'technology',
        loadChildren: () => import('./technology/technology.module').then(m => m.MicrosoftTechnologyModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MicrosoftEntityModule {}
