import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrosoftSharedModule } from 'app/shared/shared.module';
import { TechnologyComponent } from './technology.component';
import { TechnologyDetailComponent } from './technology-detail.component';
import { TechnologyUpdateComponent } from './technology-update.component';
import { TechnologyDeleteDialogComponent } from './technology-delete-dialog.component';
import { technologyRoute } from './technology.route';

@NgModule({
  imports: [MicrosoftSharedModule, RouterModule.forChild(technologyRoute)],
  declarations: [TechnologyComponent, TechnologyDetailComponent, TechnologyUpdateComponent, TechnologyDeleteDialogComponent],
  entryComponents: [TechnologyDeleteDialogComponent]
})
export class MicrosoftTechnologyModule {}
