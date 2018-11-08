/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NewLocalTestModule } from '../../../test.module';
import { HoldingDeleteDialogComponent } from 'app/entities/holding/holding-delete-dialog.component';
import { HoldingService } from 'app/entities/holding/holding.service';

describe('Component Tests', () => {
    describe('Holding Management Delete Component', () => {
        let comp: HoldingDeleteDialogComponent;
        let fixture: ComponentFixture<HoldingDeleteDialogComponent>;
        let service: HoldingService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [HoldingDeleteDialogComponent]
            })
                .overrideTemplate(HoldingDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HoldingDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HoldingService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
