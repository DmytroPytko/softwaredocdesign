import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrosoftSharedModule } from 'app/shared/shared.module';
import { ContractTypeComponent } from './contract-type.component';
import { ContractTypeDetailComponent } from './contract-type-detail.component';
import { ContractTypeUpdateComponent } from './contract-type-update.component';
import { ContractTypeDeleteDialogComponent } from './contract-type-delete-dialog.component';
import { contractTypeRoute } from './contract-type.route';

@NgModule({
  imports: [MicrosoftSharedModule, RouterModule.forChild(contractTypeRoute)],
  declarations: [ContractTypeComponent, ContractTypeDetailComponent, ContractTypeUpdateComponent, ContractTypeDeleteDialogComponent],
  entryComponents: [ContractTypeDeleteDialogComponent]
})
export class MicrosoftContractTypeModule {}
